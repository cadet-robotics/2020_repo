# Rust install instructions

**Windows**

* Install wsl (debian prefered)
* In wsl
  * Install gcc
  * Install gcc-arm-linux-gnueabi
  * Install rustup
  * Install rustup target arm-unknown-linux-gnueabi
  * Soft symlink all files in ~/.cargo/bin to /usr/bin

**Linux**

* Install gcc-arm-linux-gnueabi
* Install rustup
* Install rustup target arm-unknown-linux-gnueabi