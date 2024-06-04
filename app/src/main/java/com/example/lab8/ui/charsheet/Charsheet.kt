package com.example.lab8.ui.charsheet

class Charsheet(
    var level: Int = 1,
    var name: String = "",
    var charClass: String = "",
    var archetype: String = "",
    var strength: Int = 10,
    var dexterity: Int = 10,
    var constitution: Int = 10,
    var intelligence: Int = 10,
    var wisdom: Int = 10,
    var charisma: Int = 10,
    var experience: Int = 0,
    var armorClass: Int = 10,
    var background: String = "",
    var weapons: List<String>? = null,
    var inventory: List<String>? = null,
    var spells: List<String>? = null,
    var traits: List<String>? = null
)