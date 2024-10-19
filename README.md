## Навигация: 
* Controller (Папка с контроллерами, это самый верхний уровень, в нем прописаны роутинги для пользователя)
* Service (Папка с сервисами, в них содержится бизнес логика)
* Repository (Папка с репозиториями, самый нижний уровень, в нем написаны запросы к БД)
* Dto (Папка с data классами, просто обертки над данными для удобства)
* Model (Папка с объектами БД)

## Уровень абстракции:

### Model -> Repository -> Service -> Controller (Модели используются только в репозиториях, репозиториии только в сервисах и тд)

# Примеры: 

## Model
```kotlin
object Users : LongIdTable() {
    val username = varchar("username", 1024)
    val password = varchar("password", 1024)
    val role = enumerationByName("role", 10, Role::class)

    fun fromResultRow(row: ResultRow) = User(
        id = row[id].value,
        username = row[username],
        password = row[password],
        role = row[role]
    )
}
```

## Repository
```kotlin
interface UserRepository {
    fun findAll(): List<User>
}

class UserRepositoryImpl : UserRepository {
    override fun findAll(): List<User> = dbQuery {
        Users.selectAll()
            .map { fromResultRow(it) }
    }
}
```

### При создании репозитория нужно прописать его в файл Kodein.kt:
```kotlin
internal val repositories = DI.Module("repositories") {
    bind<UserRepository>() with singleton { UserRepositoryImpl() }
}
```

## Service
```kotlin
interface UserService {
    fun findAll(): List<User>
}

class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    override fun findAll(): List<User> {
        return userRepository.findAll()
    }
}
```

### При создании сервиса нужно прописать его в файл Kodein.kt:
```kotlin
internal val services = DI.Module("services") {
    bind<UserService>() with singleton { UserServiceImpl(instance()) }
    bind<AuthService>() with singleton { AuthServiceImpl(instance(), instance()) }
}

```

## Controller
```kotlin
class UserController(
    private val userService: UserService
) : Controller {
    
}
```
### Нажать ctrl + enter на "UserController" (там будет подчекнуто красным)
![test](src/main/resources/readme/controller-1.png)

![test](src/main/resources/readme/controller-2.png)

```kotlin
class UserController(
    private val userService: UserService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {
            
        }
}
```

```kotlin
class UserController(
    private val userService: UserService
) : Controller {
    override val setup: Routing.() -> Unit
        get() = {

            get {
                val ans = userService.findAll()
                call.respond(ans)
            }

            get("{id}") {
                val id = call.parameters.getOrFail<Long>("id")
                val ans = userService.findById(id)
                call.respond(ans)
            }

        }
}
```

### При создании контроллера нужно прописать его в файл Kodein.kt:
```kotlin
internal val controllers = DI.Module("controllers") {
    bindSet<Controller> {
        bind { singleton { UserController(instance()) } }
        bind { singleton { AuthController(instance()) } }
    }
}
```

# Kodein:
## С помощью кодеина создаем экземпляры классов, в скобочках к объекту передаем нужные ему параметры в конструктор с помощью instance():
```kotlin
internal val services = DI.Module("services") {
    bind<UserService>() with singleton { UserServiceImpl(instance()) }
}

class UserServiceImpl(
    private val userRepository: UserRepository
) 
```
## Кол-во instance() = кол-ву параметров в классе
