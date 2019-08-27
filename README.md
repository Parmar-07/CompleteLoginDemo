# CompleteLoginDemo
Hi, a very excitement for my first project in github, this a complete a necessary functionality of login in present software development  trend like RxJava,Unit Testing, and much more

# About

CompleteLoginDemo have a simple login module with SignIn & SignUp features covered following points
* MVP code architecture (kotlin)
* Retrofit + RxJava2
* Template Design Pattern
* Custom Views
* Kotlin Extension & Lambda function
* Unit Testing


<i> Apologies this is my first github project as long term of experience</i>


* <b>MVP code architecture (kotlin)</b>

As we know about MVP, its <b>MODEL-VIEW-PRESENTER</b> code architecture. Lets have a quite simple meaning to understand from top-to-bottom approach

<b>VIEW :</b> We create an interface of VIEW of UI functionality

<b>PRESENTER :</b> We create an class mediator of PRESENTER to get a data to user as per the UI functionality

<b>MODEL :</b> MODEL is data source providing to VIEW via PRESENTER


* <b>Retrofit + RxJava2</b>

<b>Retrofit :</b> As we all know retrofit is library to use the network services. 
In this project REST & XML Data Model mapping is covered

<b>RxJava2 :</b> RxJava2 <b>"functional reactive programming"</b>, at the present time this library reduces boiler plate codes 
of threading & network calls and also for reactive scenarioes.

<i>Retrofit with ok-http client</i>

<b>
    
    implementation 'com.squareup.retrofit2:retrofit:2.5.0'
    
    implementation 'com.squareup.retrofit2:converter-gson:2.5.0'
    
    implementation 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'
    
    implementation 'com.squareup.okhttp3:logging-interceptor:3.9.0'
    
    implementation 'com.squareup.retrofit2:converter-simplexml:2.5.0'
    
    
</b> 



<i>RxJava2</i>

<b>

    implementation 'io.reactivex.rxjava2:rxandroid:2.1.0'
    
</b> 



* <b>Template Design Pattern</b>

We all do some common functionality for screens like showing toast,network check,etc. Its good practice to create a base for all 
this common functionality and also reduces the boiler plate code

* <b>Custom Views</b>

For specific cases I recommend creating your own CustomView, In this I have extended some UI components to enhance 
the UI functionality

<b>EditText  :</b>

Disable copy-paste using a boolean flag <b><i>app:disableCopy="true"</i></b>

Allow a text on keyboard type with regex string <b><i>app:type_regex="<MATCH_REGEX>"</i></b>

Add a PREFIX on Edittext <b><i>app:prefix="Mr/Ms. "</i></b>

<b>Toast  :</b>

Preview in center

Categorized with : <b><i>Normal</i></b>,<b><i>Success</i></b>,<b><i>Warning</i></b>,<b><i>Error</i></b>

<b>BlurLayout  :</b> Displaying a blury background

* <b>Kotlin Extension & Lambda function</b>

<b>Kotlin Extension  :</b> Without inherit from a class, Kotlin provides the ability to extend a class <i>TRICKY!</i> 
To declare an extension function, we need to prefix its class name and extend a function with <b>"dot"</b>.

Hiding a password on keyborad type EditText.<b>hidePasswordTransForm()</b>

Creating a HTML refrence link to String.<b>applyHtmlRef()</b> 

<b>Kotlin Lambda function  :</b> Quite simple its a interface parameter to a function.

As per custom logic of network api calls using extension function

<b><i>

fun <T> Single<T>.apiSuccess(successMap: (T) -> Unit): Single<T> {

    return this.doAfterSuccess {
    
        successMap(it)
        
    }
    
}

</b></i>
successMap: (T) -> Unit is type of interface listener



<b>* Unit Testing</b>

A level of software testing where individual units/ components of a software are tested,I have covered only a API testing uisng 
JUnit & PowerMockito 

<b>

    testImplementation 'junit:junit:4.12'
    
    testImplementation "org.powermock:powermock-module-junit4:1.7.4"
    
    testImplementation "org.powermock:powermock-api-mockito2:1.7.4"
    
    testImplementation "org.mockito:mockito-core:2.8.47"
    
</b>



















