#include <jni.h>
#include <string>
#include <stdlib.h>
#include <algorithm>
#include <android/log.h>

#include <opencv2/core.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/core/ocl.hpp>
#include <opencv2/imgcodecs.hpp>

#include <GLES2/gl2.h>
#include <EGL/egl.h>

#define LOG_TAG "JNI Native Lib"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

extern "C" JNIEXPORT void JNICALL Java_independent_1study_paintcalculator_NativeBridge_testDraw(JNIEnv* env, jint texIn, jint texOut, jint width, jint height)
{
    static cv::Mat image;
    image.create(height, width, CV_8UC4);
    glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, image.data);

    /*
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texOut);
    //https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glTexSubImage2D.xhtml
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, image.data);
    */
}

extern "C" JNIEXPORT jobject JNICALL Java_independent_1study_paintcalculator_NativeBridge_blobAnalyze(JNIEnv* env, jint texIn, jint texOut, jint width, jint height,
    jint hMin, jint hMax, jint sMin, jint sMax, jint vMin, jint vMax)
{
    static cv::Mat image;
    image.create(height, width, CV_8UC4);
    glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, image.data);

    static cv::Mat hsv;
    cv::cvtColor(image, hsv, CV_RGBA2RGB);
    cv::cvtColor(hsv, hsv, CV_RGB2HSV);

    static cv::Mat thresh;
    cv::inRange(hsv, cv::Scalar(hMin, sMin, vMin), cv::Scalar(hMax, sMax, vMax), thresh);

    std::vector<std::vector<cv::Point>> contours;
    cv::findContours(thresh, contours, cv::RETR_EXTERNAL, CV_CHAIN_APPROX_SIMPLE);
    std::vector<cv::Rect> walls;

    cv::Rect largestWall;
    for (auto &contour : contours)
    {
        cv::Rect rect = cv::boundingRect(contour);

        if(largestWall.area() > rect.area())
        {
            largestWall = rect;
        }
        LOGD("Contour Loop");
        walls.push_back(rect);
    }

    jclass cls = (env)->FindClass("org/opencv/core/Rect");
    jmethodID constructor = env->GetMethodID(cls, "<init>", "(IIII)V");
    return env->NewObject(cls, constructor, largestWall.x, largestWall.y, largestWall.width, largestWall.height);
}