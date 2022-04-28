# Aether Wars: A Minecraft-Inspired Turn-Based Card Game

> Program implementasi _turn-based card game_ berdasarkan game _Minecraft / Yu-Gi-Oh!_ menggunakan bahasa Java dan menerapkan konsep-konsep _Object-Oriented Programming_ serta _Design Pattern_
> > Tugas Besar 2 IF2210 Pemrograman Berorientasi Objek
> > Semester II 2021/2022

## Deskripsi

AetherWars merupakan sebuah turn-based card game, dengan dua orang pemain. Implementasi permainan menggunakan bahasa Java dengan memanfaatkan konsep OOP. Dengan menggunakan konsep-konsep OOP dan _design pattern_, program ini dapat dimainkan oleh dua orang pemain secara lokal.

## Requirements
- <a href = "https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html">Java SE 8 (JDK 8u202)</a>

## How To Run 
1. _Clone_ repository ini terlebih dahulu
2. Navigasikan ke dalam folder `src`, lalu jalankan perintah berikut
```java
./gradlew run 
```

## Gameplay 
- Permainan dilakukan oleh dua orang pemain.
- Terdapat empat phase tiap turn :
  - ## **Draw** 
    Pemain mengambil kartu 3 kartu dari deck. Dari 3 kartu, pemain memilih satu kartu untuk digunakan, 2 kartu dikembalikan ke deck. kartu maksimal yang bisa dimiliki pemain adalah 5 kartu.
    
    ![draw](asset/phase-draw-img.gif)
   
  - ## **Plan** 
    Pemain dapat melakukan beberapa aksi :
    1. Meletakkan kartu karakter ke board.
    2. Menggunakan spell pada karakter di board.
    3. Membuang karakter yang dimiliki atau yang ada di board.
    4. Menggunakan mana atau exp.
    
    ![plan](asset/phase-plan-img.gif)

  - ## **Attack**
    Pemain dapat menyerang kartu karakter milik lawan, atau pihak lawan secara langsung (HP pihak lawan akan berkurang) Pemain bisa langsung menyerang pihak lawan apabila tidak ada kartu karakter pada _board_ lawan
    
    ![attack](asset/phase-attack-img.gif)
    
  - #### **End**
    Mengakhiri giliran seorang pemain dan mengembalikan kendali ke pemain lawan.
    
    ![end](asset/phase-end-img.gif)
    
- Permainan akan berakhir apabila salah satu pemain tidak mempunyai HP / kartu di _deck_ tersisa

## Authors
| Nama | NIM | Email |
| ----- | --- | ----------|
|Ilham Prasetyo Wibowo | 13520013 | <13520013@std.stei.itb.ac.id> |
|Gregorius Moses Marevson | 13520052 | <13520052@std.stei.itb.ac.id> |
|Amar Fadil | 13520103 | <13520103@std.stei.itb.ac.id> |
|Owen Christian Wijaya | 13520124 | <13520124@std.stei.itb.ac.id> |
|Fachry Dennis Heraldi | 13520139 | <13520139@std.stei.itb.ac.id> |

## Credit

Minecraft images and description are taken from [Minecraft Wikia](https://minecraft.fandom.com/wiki/)
