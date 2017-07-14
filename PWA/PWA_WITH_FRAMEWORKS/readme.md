PWA with ReactJS

1) Install Xcode and Xcode command lines tools.

Get Xcode from Mac App Store. Click Here

2) Install Xcode Command Line Tools, Open terminal and run following command.

xcode-select --install

3) Install Homebrew, Homebrew is a package manager to install missing package on OSX. Open terminal and run following command.

ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"

4) Install NodeJs and NPM. React comes package in NPM so we have to install it. Run following command in terminal

brew install node

After installation check with following command to see if it's installed.

node -v

npm -v

5) Next we will install React, run following command in terminal.

npm install react

6) Now we will install Command line utility to create react app. Run following command in terminal.

npm install -g create-react-app

7) Create new react app. Run following commands in terminal.

create-react-app ReactApp
cd ReactApp
npm start

PWA with ExtJs

Install Sencha Command line tools and Download ExtJs SDK from http://www.sencha.com

sencha -sdk /users/hirendave/desktop/ext-6.0.2 generate app PWA sample-pwa -modern

Add manifest information in app.json

enable cache in loader and production

sencha app build

PWA with Angular Mobile Toolkit

Install npm CLI

npm install -g angular-cli

Create New App

ng new PWA_ANGULAR --mobile

cd hello-mobile

$ ng serve

Go to http://localhost:4200/

PWA with Ionic

Install Ionic

npm install -g cordova ionic

Start an App with any of following template

ionic start PWA blank

ionic start PWA tabs

ionic start PWA sidemenu

cd PWA

ionic serve
