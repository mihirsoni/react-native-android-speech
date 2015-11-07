/* 
* @Author: mihir
* @Date:   2015-11-07
* @Last Modified by:   mihir
* @Last Modified time: 2015-11-07
*/

'use strict';

var AndroidTTS = require('react-native').NativeModules.AndroidTTS;
var RNAndroidTTS = {
  speak(args) {
    return new Promise((resolve, reject) => { 
      AndroidTTS.speak(args, (isSpeaking) =>{
        console.log(isSpeaking);
        if(typeof isSpeaking === 'boolean'){
          if (!isSpeaking) {
            reject(error);
          }
            resolve(true);  
        } else {
          reject(isSpeaking);
        }
      });
    });
  },
  isSpeaking(){
    return new Promise((resolve,reject) => {
      AndroidTTS.isSpeaking((result) => {
        resolve(result);
      });
    })
  },
  shutDown(){
  	return new Promise((resolve,reject) => {
  		AndroidTTS.shutDown((result)=>{
  			resolve(result)
  		});
  	});
  },
  stop(){
  	return new Promise((resolve,reject) => {
  		AndroidTTS.stop((result)=>{
  			resolve(result)
  		});
  	});
  },
  getLocales() {
    return new Promise((resolve, reject) => {
      AndroidTTS.getLocale((error, locales) =>{
        if (error) {
           reject(error);
        }
        resolve(locales);
      });
    });
  }
} 
module.exports = RNAndroidTTS;
