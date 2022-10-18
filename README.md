<h1 align="center">CreditCardView</h1></br>
<p align="center">
:fire: A beautifully designed fully customisable Android view that allows developers to create the UI which replicates an actual credit card. <br>
</p>
<p align="start">
  <a href="https://opensource.org/licenses/Apache-2.0"><img alt="License" src="https://img.shields.io/badge/License-Apache%202.0-blue.svg"/></a>
  <a href="https://android-arsenal.com/api?level=21"><img alt="API" src="https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat"/></a>
  <a href="https://jitpack.io/#KunikaValecha/CreditCardView"><img alt="API" src="https://jitpack.io/v/KunikaValecha/CreditCardView.svg?style=flat"/></a>
</p>

#### Inspiration
This library is inspired by the [Paweł Szymankiewicz's](https://dribbble.com/pawelszymankiewicz) work on [dribble](https://dribbble.com/shots/6440077-Add-a-New-Credit-Card-alternate-flow).

#### Index
+ [Getting started](#getting-started)
+ [Basic usage](#basic-usage)
+ [Customisation](#customisation)
+ [Functions available](#functions-available)
+ [Examples](#getting-started)

## Getting started
In your project's build.gradle

```gradle
repositories {
    maven { url "https://jitpack.io" }
}
```
</br>
In your modules's build.gradle
</br>
<br>
<a href="https://jitpack.io/#KunikaValecha/CreditCardView"><img alt="API" src="https://jitpack.io/v/KunikaValecha/CreditCardView.svg?style=flat"/></a>
</br>

```gradle
implementation "com.github.KunikaValecha:CreditCardView:$latest_version"
```

## Basic usage

```xml
<com.github.credit_card_view.CreditCardView
        android:id="@+id/creditCardView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="50dp"
        app:card_autoFocusCardNumber="true"
        app:card_backGradientEnd="#626262"
        app:card_backGradientStart="#2D2D2D"
        app:card_backStrip2Color="#303030"
        app:card_backStripColor="@color/black"
        app:card_cardExpiryHeaderColor="#C4C4C4"
        app:card_cardExpiryHintColor="#4DFFFFFF"
        app:card_cardExpiryTextColor="@color/white"
        app:card_cardNameHeaderColor="#C4C4C4"
        app:card_cardNameHintColor="#4DFFFFFF"
        app:card_cardNameTextColor="@color/white"
        app:card_cardNumberHintColor="#656565"
        app:card_cardNumberTextColor="@color/white"
        app:card_cvvBackgroundColor="@color/white"
        app:card_cvvHintColor="#4D000000"
        app:card_cvvTextColor="#000000"
        app:card_frontGradientEnd="#313131"
        app:card_frontGradientStart="#1F1F1F"
        app:card_isBackSideEditable="true"
        app:card_isFrontSideEditable="true"
        app:card_outline_base_color="#EBB46A"
        app:card_outline_error_color="#EB6A6E"
        app:layout_constraintTop_toTopOf="parent" />
```

## Customisation
| Attribute  | Type | Optional | Definition |
| ------------- | ------------- | ------------- | ------------- |
| card_autoFocusCardNumber  | boolean  | Yes | whether to automatically focus on card number input field & open keyboard |
| card_backGradientStart  | color hex  | No | start color of back side of card  |
| card_backGradientEnd  | color hex  | No  | end color of back side of card |
| card_frontGradientStart  | color hex  | No  | start color of front side of card |
| card_frontGradientEnd  | color hex  | No  | end color of front side of card |
| card_backStripColor  | color hex  | No  | top strip color of back side of card |
| card_backStrip2Color  | color hex  | No  | bottom strip color of back side of card |
| card_cvvBackgroundColor  | color hex  | Yes  | cvv input filed background color |
| card_cvvHintColor  | color hex  | Yes  | cvv input filed hint color |
| card_cvvTextColor  | color hex  | Yes  | cvv input filed text color |
| card_cardNumberTextColor  | color hex  | Yes  | card number input filed text color |
| card_cardNumberHintColor  | color hex  | Yes  | card number input filed hint color |
| card_minCardNumberLength  | integer  | Yes  | minimum length of credit card number(excluding separator) - default value is 15 chars |
| card_maxCardNumberLength  | integer  | Yes  | maximum length of credit card number(excluding separator) - default value is 19 chars |
| card_cardNameHeaderColor  | color hex  | Yes  | card name header text color |
| card_cardNameTextColor  | color hex  | Yes  | card name input field text color |
| card_cardNameHintColor  | color hex  | Yes  | card name input field hint color |
| card_cardExpiryHeaderColor  | color hex  | Yes  | card expiry header text color |
| card_cardExpiryTextColor  | color hex  | Yes  | card expiry input field text color |
| card_cardExpiryHintColor  | color hex  | Yes  | card expiry input field hint color |
| card_outline_error_color  | color hex  | Yes  | outline color in case of error |
| card_outline_base_color  | color hex  | Yes  | default outline color |
| card_separator  | string  | Yes  | separator character used to separate the card number - space by default |
| card_isFrontSideEditable  | boolean  | Yes  | whether we can edit the front side of the card |
| card_isBackSideEditable  | boolean  | Yes  | whether we can edit the back side of the card |

## Functions available
```kotlin
fun getCardNumber(): String? //Function to get card number

fun getNameOnCard(): String? //Function to get card name

fun getExpiryDate(): ExpiryDate? //Function to get expiry daye

fun getCvv(): String? //Function to get cvv

fun getCurrentCardSide(): CardSide //Function to get current card side

fun flipCard() //Function to flip card

fun setBankName(bankName: String) //Function to set bank name

fun setCardProviderLogo(logo: Bitmap) //Function to set card provider logo

fun setErrorStateToField(cardField: CardField) //Function to set error state on a specific field
```

## Find this library useful? :heart:
Support it by joining __[stargazers](https://github.com/KunikaValecha/CreditCardView/stargazers)__ for this repository. :star:

# License
```xml
Copyright 2022 KunikeValecha

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
