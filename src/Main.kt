data class Penumpang(
    val nama: String,
    val umur: Int,
    val alamat: String,
    val ruteBus: String,
    val nomorKursi: Int,
    val harga: Double
)

data class Bus(
    val idBus: String,
    val rute: String,
    val tanggalBerangkat: String,
    val waktuBerangkat: String,
    val status: String
)

data class Tiket(
    val idTiket: String,
    val rute: String,
    val harga: Double,
    var stok: Int
)

class SistemTiketBus {
    private val daftarPengguna = mutableMapOf(
        "admin" to "admin123",
        "operator1" to "op123",
        "agen1" to "agen123"
    )

    private val daftarBus = mutableListOf(
        Bus("BUS001", "Palopo-Makassar", "", "", "Aktif"),
        Bus("BUS002", "Makassar-Palopo", "", "", "Aktif")
    )

    private val daftarTiket = mutableListOf(
        Tiket("T001", "Palopo-Makassar", 150000.0, 40),
        Tiket("T002", "Makassar-Palopo", 150000.0, 40)
    )

    private val kursiTersedia = mutableMapOf(
        "BUS001" to (1..20).toMutableList(),
        "BUS002" to (1..20).toMutableList()
    )

    fun mulai() {
        while (true) {
            println("\n=== SISTEM PEMESANAN TIKET BUS ===")
            println("1. Penumpang")
            println("2. Operator Bus")
            println("3. Agen Tiket")
            println("4. Keluar")
            print("Pilih menu (1-4): ")

            when (readLine()) {
                "1" -> menuPenumpang()
                "2" -> menuOperatorBus()
                "3" -> menuAgenTiket()
                "4" -> return
                else -> println("Menu tidak valid!")
            }
        }
    }

    private fun menuPenumpang() {
        println("\n=== MENU PENUMPANG ===")
        print("Masukkan nama: ")
        val nama = readLine() ?: return
        print("Masukkan umur: ")
        val umur = readLine()?.toIntOrNull() ?: return
        print("Masukkan alamat: ")
        val alamat = readLine() ?: return

        println("\nRute yang tersedia:")
        daftarBus.forEachIndexed { index, bus ->
            println("${index + 1}. ${bus.rute}")
        }

        print("Pilih rute (1-${daftarBus.size}): ")
        val ruteIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (ruteIndex !in daftarBus.indices) {
            println("Rute tidak valid!")
            return
        }

        val busYangDipilih = daftarBus[ruteIndex]
        println("\nKursi tersedia: ${kursiTersedia[busYangDipilih.idBus]}")
        print("Pilih nomor kursi: ")
        val nomorKursi = readLine()?.toIntOrNull() ?: return

        if (nomorKursi !in kursiTersedia[busYangDipilih.idBus]!!) {
            println("Kursi tidak tersedia!")
            return
        }

        val tiket = daftarTiket.find { it.rute == busYangDipilih.rute }!!
        kursiTersedia[busYangDipilih.idBus]?.remove(nomorKursi)
        tiket.stok--

        val penumpangBaru = Penumpang(nama, umur, alamat, busYangDipilih.rute, nomorKursi, tiket.harga)
        println("\nDetail Pemesanan:")
        println("Nama: ${penumpangBaru.nama}")
        println("Rute: ${penumpangBaru.ruteBus}")
        println("Kursi: ${penumpangBaru.nomorKursi}")
        println("Harga: Rp${penumpangBaru.harga}")
    }

    private fun menuOperatorBus() {
        println("\n=== LOGIN OPERATOR BUS ===")
        print("Username: ")
        val username = readLine() ?: return
        print("Password: ")
        val password = readLine() ?: return

        if (daftarPengguna[username] != password || !username.startsWith("operator")) {
            println("Login gagal!")
            return
        }

        while (true) {
            println("\n=== MENU OPERATOR ===")
            println("1. Update Jadwal Bus")
            println("2. Update Status Pemeliharaan")
            println("3. Kembali")
            print("Pilih menu (1-3): ")

            when (readLine()) {
                "1" -> updateJadwalBus()
                "2" -> updatePemeliharaan()
                "3" -> return
                else -> println("Menu tidak valid!")
            }
        }
    }

    private fun updateJadwalBus() {
        println("\nDaftar Bus:")
        daftarBus.forEachIndexed { index, bus ->
            println("${index + 1}. ${bus.idBus} - ${bus.rute}")
        }

        print("Pilih bus (1-${daftarBus.size}): ")
        val busIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (busIndex !in daftarBus.indices) {
            println("Bus tidak valid!")
            return
        }

        print("Masukkan tanggal berangkat (DD/MM/YYYY): ")
        val tanggal = readLine() ?: return
        print("Masukkan waktu berangkat (HH:MM): ")
        val waktu = readLine() ?: return

        val busUpdate = daftarBus[busIndex].copy(
            tanggalBerangkat = tanggal,
            waktuBerangkat = waktu
        )
        daftarBus[busIndex] = busUpdate
        println("Jadwal berhasil diupdate!")
    }

    private fun updatePemeliharaan() {
        println("\nDaftar Bus:")
        daftarBus.forEachIndexed { index, bus ->
            println("${index + 1}. ${bus.idBus} - ${bus.rute} - ${bus.status}")
        }

        print("Pilih bus (1-${daftarBus.size}): ")
        val busIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (busIndex !in daftarBus.indices) {
            println("Bus tidak valid!")
            return
        }

        println("\nJenis Pemeliharaan:")
        println("1. Service Bus")
        println("2. Cuci Bus")
        print("Pilih jenis pemeliharaan (1-2): ")

        val status = when (readLine()) {
            "1" -> "Dalam Service"
            "2" -> "Sedang Dicuci"
            else -> return
        }

        val busUpdate = daftarBus[busIndex].copy(status = status)
        daftarBus[busIndex] = busUpdate
        println("Status pemeliharaan berhasil diupdate!")
    }

    private fun menuAgenTiket() {
        println("\n=== LOGIN AGEN TIKET ===")
        print("Username: ")
        val username = readLine() ?: return
        print("Password: ")
        val password = readLine() ?: return

        if (daftarPengguna[username] != password || !username.startsWith("agen")) {
            println("Login gagal!")
            return
        }

        while (true) {
            println("\n=== MENU AGEN TIKET ===")
            println("1. Update Stok Tiket")
            println("2. Update Harga Tiket")
            println("3. Kembali")
            print("Pilih menu (1-3): ")

            when (readLine()) {
                "1" -> updateStokTiket()
                "2" -> updateHargaTiket()
                "3" -> return
                else -> println("Menu tidak valid!")
            }
        }
    }

    private fun updateStokTiket() {
        println("\nDaftar Tiket:")
        daftarTiket.forEachIndexed { index, tiket ->
            println("${index + 1}. ${tiket.idTiket} - ${tiket.rute} - Stok: ${tiket.stok}")
        }

        print("Pilih tiket (1-${daftarTiket.size}): ")
        val tiketIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (tiketIndex !in daftarTiket.indices) {
            println("Tiket tidak valid!")
            return
        }

        print("Masukkan jumlah stok baru: ")
        val stokBaru = readLine()?.toIntOrNull() ?: return

        daftarTiket[tiketIndex].stok = stokBaru
        println("Stok tiket berhasil diupdate!")
    }

    private fun updateHargaTiket() {
        println("\nDaftar Tiket:")
        daftarTiket.forEachIndexed { index, tiket ->
            println("${index + 1}. ${tiket.idTiket} - ${tiket.rute} - Harga: Rp${tiket.harga}")
        }

        print("Pilih tiket (1-${daftarTiket.size}): ")
        val tiketIndex = readLine()?.toIntOrNull()?.minus(1) ?: return
        if (tiketIndex !in daftarTiket.indices) {
            println("Tiket tidak valid!")
            return
        }

        print("Masukkan harga baru: ")
        val hargaBaru = readLine()?.toDoubleOrNull() ?: return

        val tiketUpdate = daftarTiket[tiketIndex].copy(harga = hargaBaru)
        daftarTiket[tiketIndex] = tiketUpdate
        println("Harga tiket berhasil diupdate!")
    }
}

fun main() {
    val sistem = SistemTiketBus()
    sistem.mulai()
}