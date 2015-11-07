/* 
* @Author: mihir
* @Date:   2015-11-07
* @Last Modified by:   mihir
* @Last Modified time: 2015-11-08
*/

'use strict';

var AndroidTTS = require('react-native').NativeModules.AndroidTTS;
var RNAndroidTTS = {
  speak(args) {
    return new Promise((resolve, reject) => { 
      AndroidTTS.speak(args, (error,result) =>{
          if (error) {
            reject(error);
          } else {
          	resolve(result);
          }
        });
    });
  },
  isSpeaking(){
    return new Promise((resolve,reject) => {
      AndroidTTS.isSpeaking((error,result) => {
      	if(error) {
      		reject(error)
      	} else {
      		resolve(result);	
      	}
      });
    })
  },
  shutDown(){
  	return new Promise((resolve,reject) => {
  		AndroidTTS.shutDown((error,result)=>{
  			if(error) {
      			reject(error)
      		} else {
      			resolve(result);	
      		}
  		});
  	});
  },
  stop(){
  	return new Promise((resolve,reject) => {
  		AndroidTTS.stop((error,result)=>{
  			if(error) {
      			reject(error)
      		} else {
      			resolve(result);	
      		}
  		});
  	});
  },
  getLocales() {
    return new Promise((resolve, reject) => {
      AndroidTTS.getLocale((error, results) =>{
        if (error) {
           reject(error);
        } else {
        	resolve(results);	
        }
        
      });
    });
  }
} 
module.exports = RNAndroidTTS;
