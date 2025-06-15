CREATE TABLE nutrify.recipes
(
    id          bigserial     NOT NULL,
    "name"      varchar(255)  NOT NULL,
    description varchar(1024) NOT NULL,
    calories    int4          NOT NULL,
    protein     int4          NOT NULL,
    fats        int4          NOT NULL,
    carbs       int4          NOT NULL,
    image       varchar(1024) NULL,
    CONSTRAINT recipes_pkey PRIMARY KEY (id)
);

CREATE TABLE nutrify.users
(
    id         bigserial     NOT NULL,
    "name"     varchar(255)  NOT NULL,
    email      varchar(1024) NOT NULL,
    "password" varchar(1024) NOT NULL,
    "role"     varchar(10)   NOT NULL,
    CONSTRAINT users_pkey PRIMARY KEY (id)
);

CREATE TABLE nutrify.ingredients
(
    id          bigserial    NOT NULL,
    "name"      varchar(255) NOT NULL,
    weight      int4         NOT NULL,
    weight_type varchar(10)  NOT NULL,
    recipe_id   int8         NOT NULL,
    CONSTRAINT ingredients_pkey PRIMARY KEY (id),
    CONSTRAINT fk_ingredients_recipe_id__id FOREIGN KEY (recipe_id) REFERENCES nutrify.recipes (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE nutrify.meail_time
(
    id        bigserial   NOT NULL,
    recipe_id int8        NOT NULL,
    "type"    varchar(15) NOT NULL,
    CONSTRAINT meail_time_pkey PRIMARY KEY (id),
    CONSTRAINT fk_meail_time_recipe_id__id FOREIGN KEY (recipe_id) REFERENCES nutrify.recipes (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE nutrify.meal_diet
(
    id        bigserial   NOT NULL,
    user_id   int8        NOT NULL,
    "type"    varchar(20) NOT NULL,
    recipe_id int8        NOT NULL,
    "date"    date        NOT NULL,
    CONSTRAINT meal_diet_pkey PRIMARY KEY (id),
    CONSTRAINT fk_meal_diet_recipe_id__id FOREIGN KEY (recipe_id) REFERENCES nutrify.recipes (id) ON DELETE RESTRICT ON UPDATE RESTRICT,
    CONSTRAINT fk_meal_diet_user_id__id FOREIGN KEY (user_id) REFERENCES nutrify.users (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);

CREATE TABLE nutrify.personal_data
(
    id         bigserial                                                                NOT NULL,
    user_id    int8                                                                     NOT NULL,
    age        int4                                                                     NOT NULL,
    height     numeric(10, 1)                                                           NOT NULL,
    weight     numeric(10, 2)                                                           NOT NULL,
    sex        varchar(10)                                                              NOT NULL,
    "target"   varchar(20)                                                              NOT NULL,
    activity   varchar(20)                                                              NOT NULL,
    calories   int4                                                                     NOT NULL,
    updated_at timestamp DEFAULT now()                                                  NOT NULL,
    CONSTRAINT personal_data_pkey PRIMARY KEY (id),
    CONSTRAINT fk_personal_data_user_id__id FOREIGN KEY (user_id) REFERENCES nutrify.users (id) ON DELETE RESTRICT ON UPDATE RESTRICT
);