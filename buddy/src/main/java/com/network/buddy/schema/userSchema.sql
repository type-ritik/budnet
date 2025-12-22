CREATE TABLE "users"
(
    "id" SERIAL,
    "name" VARCHAR(30) NOT NULL,
    "username" VARCHAR(20) NOT NULL UNIQUE CHECK(LENGTH("username") >= 3),
    "email" VARCHAR(40) NOT NULL UNIQUE,
    "password" VARCHAR(20) NOT NULL CHECK(LENGTH("password") >= 8),
    "isAdmin" BOOLEAN NOT NULL DEFAULT FALSE,
    "createdAt" TIMESTAMP NOT NULL DEFAULT now(),
    PRIMARY KEY("id")
)
