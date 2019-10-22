extern crate jni;

use jni::JNIEnv;
use jni::objects::JObject;
use std::os::raw::c_int;

#[cfg(test)]
mod tests {
    #[test]
    fn it_works() {
        assert_eq!(2 + 2, 4);
    }
}

#[no_mangle]
pub extern "system" fn Java_frc_robot_JNI_test(_env: JNIEnv, _obj: JObject, var: c_int) -> c_int {
    var * var * var
}