#include <jni.h>
#include <string>
#include <android/log.h>
extern "C" JNIEXPORT jstring JNICALL
Java_com_mteam_movietrailer_NativeKey_getAuthenKey(JNIEnv *env, jobject thiz) {
    std::string hello = "Hello from C++";

//    jclass clazz = env->FindClass("com/mteam/movietrailer/NativeKey");
//    jfieldID fieldId = env->GetStaticFieldID(clazz, "static", "I");
//    if (fieldId == NULL) {
//        __android_log_print(ANDROID_LOG_INFO,  __FUNCTION__, "fieldId == null");
//    } else {
//        jint fieldValue = env->GetStaticIntField(clazz, fieldId);
//        __android_log_print(ANDROID_LOG_INFO,  __FUNCTION__, "Field value: %d ", fieldValue); //Field value: 3
//    }

    return env->NewStringUTF(hello.c_str());
}



extern "C" JNIEXPORT jobjectArray JNICALL
Java_com_mteam_movietrailer_NativeKey_getAuthenKeyTrakt (JNIEnv *env, jobject jobj){

//    jclass clazz = env->FindClass("com/mteam/movietrailer/NativeKey");
//    jmethodID methodId = env->GetStaticMethodID(clazz, "staticMethod", "(I)I");
//    if (methodId == NULL) {
//        __android_log_print(ANDROID_LOG_INFO,  __FUNCTION__, "methodId == null");
//    } else {
//        jint value = env->CallStaticIntMethod(clazz, methodId, 4);
//        __android_log_print(ANDROID_LOG_INFO,  __FUNCTION__, "value: %d ", value); //value: 4
//    }

    jobjectArray ret;
    int i;
    char *data[2]= {"CLIENT_ID", "Client_Secretkey"};
    ret= (jobjectArray)env->NewObjectArray(2,env->FindClass("java/lang/String"),env->NewStringUTF(""));
    for(i=0;i<2;i++) env->SetObjectArrayElement(ret,i,env->NewStringUTF(data[i]));

    return(ret);
}