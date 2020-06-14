package fr.ec.arridle

enum class Adjective {
    Smart,
    Big,
    Giant,
    Small,
    Ferocious,
    Stylish,
    Magnificent,
    Extravagant,
    Great,
    Sunny,
    Brave,
    Lovely,
    Famous,
    Discrete,
    Eloquent
}

enum class Color {
    Red,
    Black,
    Green,
    Blue,
    Yellow,
    White,
    Grey,
    Orange,
    Rosa,
    Purple,
    Brown,
    Magenta,
    Beige,
    Dark,
    Light,
}


enum class Animal {
    Cat,
    Dog,
    Dolphin,
    Bird,
    Lion,
    Bug,
    Elephant,
    Bear,
    Tiger,
    Wolf,
    Bunny,
    Panda,
    Snake,
    Mouse,
    Shark
}

fun randomPseudo(): String {
    return Adjective.values().random().toString() + Color.values().random().toString() + Animal.values().random().toString()
}