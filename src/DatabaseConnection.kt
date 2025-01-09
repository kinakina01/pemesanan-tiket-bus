import java.sql.DriverManager

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val url = "jdbc:mysql://localhost:3306/pemesanan_tiket"
        val user = "kelompok4"
        val password = "@kinakina2005"

        try {
            val con = DriverManager.getConnection(url, user, password)
            println("Connection Successful: $con")
        } catch (var5: Exception) {
            val e = var5
            e.printStackTrace()
        }

    }
}