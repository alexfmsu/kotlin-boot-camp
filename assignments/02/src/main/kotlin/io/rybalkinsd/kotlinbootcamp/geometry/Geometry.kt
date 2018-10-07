package io.rybalkinsd.kotlinbootcamp.geometry

/**
 * Entity that can physically intersect, like flame and player
 */
interface Collider {
    fun isColliding(other: Collider): Boolean
    fun isIn(x: Int, y: Int): Boolean
    fun FirstCornerX(): Int
    fun FirstCornerY(): Int
    fun SecondCornerX(): Int
    fun SecondCornerY(): Int
}

/**
 * 2D point with integer coordinates
 */
class Point(x: Int, y: Int) : Collider {
    val x = x
    val y = y

    override fun equals(other: Any?): Boolean = other is Point && this.x == other.x && this.y == other.y

    override fun FirstCornerX(): Int = this.x
    override fun FirstCornerY(): Int = this.y
    override fun SecondCornerX(): Int = this.x
    override fun SecondCornerY(): Int = this.y

    override fun isColliding(other: Collider): Boolean = other.isIn(this.x, this.y)

    override fun isIn(x: Int, y: Int): Boolean = this.x == x && this.y == y
}

/**
 * Bar is a rectangle, which borders are parallel to coordinate axis
 * Like selection bar in desktop, this bar is defined by two opposite corners
 * Bar is not oriented
 * (It does not matter, which opposite corners you choose to define bar)
 */
class Bar(firstCornerX: Int, firstCornerY: Int, secondCornerX: Int, secondCornerY: Int) : Collider {
    val fCornerX: Int
    val fCornerY: Int
    val sCornerX: Int
    val sCornerY: Int

    init {
        fCornerX = minOf(firstCornerX, secondCornerX)
        sCornerX = maxOf(firstCornerX, secondCornerX)
        fCornerY = minOf(firstCornerY, secondCornerY)
        sCornerY = maxOf(firstCornerY, secondCornerY)
    }

    override fun equals(other: Any?): Boolean = other is Bar &&
            this.fCornerX == other.fCornerX &&
            this.fCornerY == other.fCornerY &&
            this.sCornerX == other.sCornerX &&
            this.sCornerY == other.sCornerY

    override fun FirstCornerX(): Int = this.fCornerX
    override fun FirstCornerY(): Int = this.fCornerY
    override fun SecondCornerX(): Int = this.sCornerX
    override fun SecondCornerY(): Int = this.sCornerY

    override fun isColliding(other: Collider): Boolean = other.isIn(this.fCornerX, this.fCornerY) ||
            other.isIn(this.sCornerX, this.sCornerY) ||
            other.isIn(this.fCornerX, this.sCornerY) ||
            other.isIn(this.sCornerX, this.fCornerY) ||
            this.isIn(other.FirstCornerX(), other.FirstCornerY()) ||
            this.isIn(other.SecondCornerX(), other.SecondCornerY()) ||
            this.isIn(other.FirstCornerX(), other.SecondCornerY()) ||
            this.isIn(other.SecondCornerX(), other.FirstCornerY())

    override fun isIn(x: Int, y: Int): Boolean = (this.fCornerX <= x).xor(this.sCornerX <= x) && (this.fCornerY <= y).xor(this.sCornerY <= y)
}