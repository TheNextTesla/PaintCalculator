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

    LOGD("Test Read Complete %d", image.data[0]);

    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texOut);
    //https://www.khronos.org/registry/OpenGL-Refpages/gl4/html/glTexSubImage2D.xhtml
    glTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, image.data);
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

    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    glEnable(GL_TEXTURE_2D);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texOut);
    /*glBegin(GL_LINES);
       glVertex3f(largestWall.x, largestWall.y, 0);
       glVertex3f(largestWall.x + largestWall.width, largestWall.y + largestWall.height, 0);
    glEnd();
    glBegin(GL_LINES);
       glVertex3f(largestWall.x, largestWall.y, 0);
       glVertex3f(largestWall.x, largestWall.y + largestWall.height, 0);
    glEnd();
     glBegin(GL_LINES);
       glVertex3f(largestWall.x + largestWall.width, largestWall.y, 0);
       glVertex3f(largestWall.x + largestWall.width, largestWall.y + largestWall.height, 0);
    glEnd();
     glBegin(GL_LINES);
       glVertex3f(largestWall.x, largestWall.y + largestWall.height, 0);
       glVertex3f(largestWall.x + largestWall.width, largestWall.y + largestWall.height, 0);
    glEnd();*/
    /*glBegin(GL_QUADS);
    glVertex2f(1.0f, 1.0f);
    glVertex2f(2.0f, 1.0f);
    glVertex2f(2.0f, 2.0f);
    glVertex2f(1.0f, 2.0f);
    glEnd();*/
    float vertices[] = {
         0.0f,  0.5f, // Vertex 1 (X, Y)
         0.5f, -0.5f, // Vertex 2 (X, Y)
        -0.5f, -0.5f,  // Vertex 3 (X, Y)
        0.5f, 0.5f
    };
    GLuint vbo;
    glGenBuffers(1, &vbo);
    glBindBuffer(GL_ARRAY_BUFFER, vbo);
    glBufferData(GL_ARRAY_BUFFER, sizeof(vertices), vertices, GL_STREAM_DRAW);

    const char* vertexShaderCode = R"glsl(
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            // The matrix must be included as a modifier of gl_Position.
            // Note that the uMVPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}")glsl";

    /*
    const char* fragmentShaderCode = R"glsl("precision mediump float;" +
                 "uniform vec4 vColor;" +
                 "void main() {" +
                 "  gl_FragColor = vColor;" +
                 "}")glsl";

    GLuint vertexShader = glCreateShader(GL_VERTEX_SHADER);
    glShaderSource(vertexShader, 1, &vertexShaderCode, NULL);
    glCompileShader(vertexShader);

    GLuint fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
    glShaderSource(fragmentShader, 1, &fragmentShaderCode, NULL);
    glCompileShader(fragmentShader);

    GLuint shaderProgram = glCreateProgram();
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    

    glLinkProgram(shaderProgram);
    glUseProgram(shaderProgram);

    GLint posAttrib = glGetAttribLocation(shaderProgram, "position");
    glVertexAttribPointer(posAttrib, 2, GL_FLOAT, GL_FALSE, 0, 0);
    glEnableVertexAttribArray(posAttrib);

    GLuint vao;
    glGenVertexArrays(1, &vao);
    glBindVertexArray(vao);

    glDrawArrays(GL_QUADS, 0, 3);

    glDisable(GL_TEXTURE_2D);
    */
    
    jclass cls = (env)->FindClass("org/opencv/core/Rect");
    jmethodID constructor = env->GetMethodID(cls, "<init>", "(IIII)V");
    return env->NewObject(cls, constructor, largestWall.x, largestWall.y, largestWall.width, largestWall.height);
}
