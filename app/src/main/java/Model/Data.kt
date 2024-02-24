package Model

class Data {
    var amount: Int = 0
    var type: String? = null
    var note: String? = null
    var id: String? = null
    var date: String? = null

    constructor()

    constructor(amount: Int, type: String, note: String, id: String, date: String) {
        this.amount = amount
        this.type = type
        this.note = note
        this.id = id
        this.date = date
    }

    // Getters and Setters
    fun getAmount(): Int {
        return amount
    }

    fun setAmount(amount: Int) {
        this.amount = amount
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getNote(): String? {
        return note
    }

    fun setNote(note: String) {
        this.note = note
    }

    fun getId(): String? {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getDate(): String? {
        return date
    }

    fun setDate(date: String) {
        this.date = date
    }
}