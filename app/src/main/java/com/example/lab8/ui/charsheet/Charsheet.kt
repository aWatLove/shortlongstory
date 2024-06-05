package com.example.lab8.ui.charsheet

class Charsheet(
    var author: String = "",
    var level: Int = 1,
    var name: String = "",
    var charClass: String = "",
    var race: String = "",
    var strength: Int = 10,
    var dexterity: Int = 10,
    var constitution: Int = 10,
    var intelligence: Int = 10,
    var wisdom: Int = 10,
    var charisma: Int = 10,
    var experience: Int = 0,
    var armorClass: Int = 10,
    var initiative: Int = 0,
    var speed: Int = 30,
    var hits: Int = 0,
    var background: String = "",
    var weapons: List<String>? = null,
    var inventory: List<String>? = null,
    var spells: List<String>? = null,
    var traits: List<String>? = null,
    var bioinfo: String = ""
)