# TP6 DPBO 2526 C2 - Flappy Bird

## Janji
Saya Muhammad Hanif Muyassar dengan NIM 2510593 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain Program

### Struktur Kelas

Program ini menggunakan pendekatan berbasis objek dengan pembagian peran sebagai berikut:

| Kelas | Deskripsi |
|---|---|
| `App.java` | Entry point program. Mengatur `JFrame`, menampilkan `MenuPanel`, dan berperan sebagai penghubung untuk memulai permainan. |
| `MenuPanel.java` | Panel menu awal berisi latar, logo, burung, serta tombol "Mulai Main" dan "Keluar" dengan gaya pixel. |
| `View.java` | Panel tampilan permainan (extends `JPanel`). Bertanggung jawab menggambar background, base, pipa, burung (dengan animasi sayap dan rotasi sesuai kecepatan), skor, serta layar Game Over. |
| `Logic.java` | Inti logika permainan (implements `ActionListener`, `KeyListener`). Mengatur game loop, gravitasi, lompatan, spawn pipa, deteksi tabrakan, perhitungan skor, restart, serta integrasi suara. |
| `Player.java` | Model burung. Menyimpan posisi, ukuran, gambar, dan kecepatan vertikal (`velocityY`). |
| `Pipe.java` | Model pipa. Menyimpan posisi, ukuran, gambar, kecepatan, serta status `passed` untuk menghitung skor. |
| `Assets.java` | Helper untuk memuat, menskalakan (dengan `NEAREST_NEIGHBOR` untuk pixel art), dan membalik gambar secara vertikal. |
| `SoundPlayer.java` | Helper untuk memuat dan memutar efek suara `.wav`. |

### Aset

Aset disimpan di `src/assets/` (dan juga tercopy ke `out/production/FlappyBird/assets/`):
- Background: `background-day.png`
- Base/tanah: `base.png`
- Burung: `yellowbird-upflap.png`, `yellowbird-midflap.png`, `yellowbird-downflap.png`
- Pipa: `pipe-green.png`
- UI: `message.png`, `gameover.png`
- Angka skor: `numbers/0.png` - `numbers/9.png`
- Suara: `wing.wav`, `point.wav`, `hit.wav`, `die.wav`, `swoosh.wav`

### Konfigurasi Permainan

| Parameter | Nilai | Keterangan |
|---|---|---|
| Ukuran frame | 360 x 640 | Resolusi tampilan permainan |
| Gravitasi | 0.4 | Percepatan jatuh burung |
| Lompatan (`jumpVelocity`) | -6.5 | Kecepatan awal saat menekan Space (negatif = ke atas) |
| Kecepatan pipa | -2 | Gerakan pipa ke kiri |
| Jarak spawn pipa | 1500 ms | Interval kemunculan pasangan pipa (Timer) |
| Game loop | 60 FPS | `Timer(1000/60, ...)` |
| Tinggi tanah | `groundY = 500` | Batas collision dengan tanah |

## Penjelasan Alur

### Alur Awal
1. Program dijalankan melalui `App.main()`.
2. `JFrame` dibuat berjudul "FlappyBird", tidak bisa di-resize.
3. `MenuPanel` ditambahkan ke frame dengan callback `() -> startGame()`.
4. Saat tombol "Mulai Main" ditekan, `App.startGame()` dipanggil.

### Alur Memulai Permainan
1. `frame.getContentPane().removeAll()` menghapus menu.
2. Dibuat objek `Logic logika = new Logic()` yang menginisialisasi:
   - Frame burung (`birdFrames`), posisi awal burung, dan `playerY` (posisi presisi untuk gerakan halus)
   - Gambar pipa atas/bawah (pipa bawah asli, pipa atas hasil flip vertikal)
   - Semua `SoundPlayer` dan memutar `swooshSound`
   - `pipesCooldown` Timer untuk spawn pipa tiap 1500ms
   - `gameloop` Timer 60 FPS untuk update permainan
3. Dibuat objek `View tampilan = new View(logika)`, lalu `logika.setView(tampilan)`.
4. `View` dimuat dengan aset, menyiapkan `scoreLabel` untuk menampilkan skor berbentuk gambar angka, menambahkan `KeyListener` dari `Logic`.
5. `View` ditambahkan ke frame, difokuskan agar bisa menerima input keyboard.

### Alur Permainan
1. **Game Loop (60 FPS)**: `Logic.actionPerformed()` dipanggil tiap frame. Jika belum game over, `move()` dijalankan lalu `view.repaint()`.
2. **Gravitasi & Gerakan Burung**: Pada `move()`, `velocityY` bertambah dengan gravitasi, `playerY` diperbarui secara presisi, lalu posisi burung disinkronkan. Burung berputar sesuai `velocityY` (maks -30° saat naik, maks 90° saat jatuh).
3. **Kontrol**: Tekan `SPACE` untuk lompat - mengatur `velocityY = jumpVelocity` dan memutar suara `wing`. Animasi sayap berputar berdasarkan urutan `up`, `mid`, `down`, `mid` setiap ~120ms.
4. **Spawn Pipa**: `pipesCooldown` memanggil `placePipes()` tiap 1500ms. Posisi Y pipa atas diacak dengan celah (`openingSpace = frameHeight/4`) untuk membentuk pasangan pipa atas-bawah.
5. **Pergerakan & Deteksi**: Setiap pipa bergerak ke kiri (`posX += pipeVelocityX`). Jika terjadi tabrakan antara burung dan pipa → `endGame(hitSound)`. Jika burung melewati tepi kanan pipa atas (belum `passed`) → skor bertambah, update tampilan skor (gambar angka), putar `pointSound`, tandai `passed = true`.
6. **Pembersihan**: Pipa yang sudah keluar layar dihapus dengan `pipes.removeIf(...)`.
7. **Collision dengan Tanah**: Jika burung menyentuh `groundY` → posisinya dikoreksi dan `endGame(dieSound)`.
8. **Game Over**: Saat game over, game loop tetap repaint tapi `move()` tidak dijalankan. Layar menampilkan gambar `gameover.png` dan teks "Tekan R untuk restart". Tekan `R` untuk memanggil `restart()`.

### Alur Restart
1. Burung dikembalikan ke posisi awal, `velocityY` direset, `playerY` direset.
2. Semua pipa dihapus, skor direset jadi 0, `view.updateScore(0)`.
3. `gameOver = false`, putar `swooshSound`, `pipesCooldown` direstart.

## Dokumentasi



### Kontrol Permainan

| Tombol | Fungsi |
|---|---|
| `SPACE` | Lompat (hanya saat permainan belum game over) |
| `R` | Restart permainan (hanya saat game over) |
| Tombol UI | "Mulai Main" untuk memulai, "Keluar" untuk menutup aplikasi |

### Fitur
- Menu awal dengan tombol bergaya pixel
- Animasi sayap burung (3 frame)
- Rotasi burung berdasarkan kecepatan vertikal
- Spawn pipa acak dengan celah
- Sistem skor berbasis jumlah pipa yang berhasil dilewati
- Deteksi tabrakan dengan pipa dan tanah
- Efek suara (lompat, skor, tabrakan, jatuh, transisi menu)
- Tampilan Game Over dengan instruksi restart
- Skor ditampilkan menggunakan gambar angka (mirip Flappy Bird asli)