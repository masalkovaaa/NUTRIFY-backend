package com.example.plugins

import com.amazonaws.SdkClientException
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.example.WebStarter
import com.example.app.controller.*
import com.example.app.dto.channel.GenerateDietMessage
import com.example.app.repository.*
import com.example.app.repository.impl.*
import com.example.app.service.*
import com.example.app.service.impl.*
import com.example.plugins.config.Controller
import com.example.plugins.config.AppConfig
import com.typesafe.config.ConfigFactory
import io.github.config4k.extract
import kotlinx.coroutines.channels.Channel
import org.kodein.di.*

internal val controllers = DI.Module("controllers") {
    bindSet<Controller> {
        bind { singleton { AuthController(instance()) } }
        bind { singleton { FoodController(instance()) } }
        bind { singleton { UserController(instance()) } }
        bind { singleton { PersonalDataController(instance()) } }
        bind { singleton { RecipeController(instance()) } }
        bind { singleton { AdminController(instance()) } }
    }
}

internal val services = DI.Module("services") {
    importOnce(channel)
    bind<UserService>() with singleton { UserServiceImpl(instance(), instance()) }
    bind<AuthService>() with singleton { AuthServiceImpl(instance(), instance(), instance(), instance()) }
    bind<FoodService>() with singleton { FoodServiceImpl(instance(), instance(), instance(), instance(), instance(), instance(), instance()) }
    bind<UploadService>() with singleton { UploadServiceImpl(instance(), instance()) }
    bind<PersonalDataService>() with singleton { PersonalDataServiceImpl(instance(), instance()) }
    bind<RecipeService>() with singleton { RecipeServiceImpl(instance()) }
    bind<AdminService>() with singleton { AdminServiceImpl(instance()) }
}

internal val repositories = DI.Module("repositories") {
    bind<UserRepository>() with singleton { UserRepositoryImpl() }
    bind<PersonalDataRepository>() with singleton { PersonalDataRepositoryImpl() }
    bind<RecipeRepository>() with singleton { RecipeRepositoryImpl() }
    bind<IngredientRepository>() with singleton { IngredientRepositoryImpl() }
    bind<MealTimeRepository>() with singleton { MealTimeRepositoryImpl() }
    bind<MealDietRepository>() with singleton { MealDietRepositoryImpl() }
    bind<AdminRepository>() with singleton { AdminRepositoryImpl() }
}

internal val s3 = DI.Module("s3") {
    bind<AmazonS3>() with singleton {
        val s3Properties = instance<AppConfig>().s3
        val amazonS3: AmazonS3
        try {
            amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(
                    AwsClientBuilder.EndpointConfiguration(s3Properties.serviceEndpoint, s3Properties.region)
                )
                .withCredentials(
                    AWSStaticCredentialsProvider(BasicAWSCredentials(s3Properties.accessKeyId, s3Properties.secretAccessKey))
                )
                .build()
        } catch (e: SdkClientException) {
            throw SdkClientException(e.message)
        }

        return@singleton amazonS3
    }
}

internal val channel = DI.Module("channel") {
    bind<Channel<GenerateDietMessage>>() with singleton { Channel() }
}

val kodein = DI {

    importOnce(controllers)
    importOnce(services)
    importOnce(repositories)
    importOnce(s3)
    importOnce(channel)

    bind { singleton { ConfigFactory.load().extract<AppConfig>("app") } }
    bind<WebStarter>() with singleton { WebStarter(instance(), instance()) }
}
