# DismissibleImageView

This is one of my favourite UX patterns which is currently being used in the Twitter app. The idea is simple, click an image for a full screen view then just move the image off the screen to go back to your previous view.

<div style="text-align:center">![gif](/assets/sample.gif)</div>
<br/>

I know this is a really simple library BUT it would be nice if more apps started adopting this kind of UX patterns that make the Android experience a little bit more enjoyable. And as I'm writting this I think I may create a few other libraries for other simple UX an UI patterns that might make your app's experience a little better.

## Installation

Add this in your root build.gradle at the end of repositories:
```
  allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
  }
```
Then add the dependency to your app's build.gradle.

```
  compile 'com.github.dmallcott:DismissibleImageView:1.0.0'
```


## How to use

Instead of using ImageViews just use DismissibleImageView and you're good to go:

```xml
<com.dmallcott.dismissibleimageview.DismissibleImageView
        android:id="@+id/activity_main_dismissibleImageView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```

Keep in mind that for the moment it will keep `adjustViewBounds` as true by default. This is because I'm working with bitmaps by default and I was too lazy to remove the extra space manually. I will definitely fix that later.

## To Do
* See issues!

## License

Copyright (C) 2017 Daniel Mallcott

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
