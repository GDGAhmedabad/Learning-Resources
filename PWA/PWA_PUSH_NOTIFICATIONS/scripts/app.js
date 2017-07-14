// Copyright 2016 Google Inc.
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//      http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


(function() {
  'use strict';

	var app = {
    	isLoading: true,
    	db: null,
    	promiseSuccess: false,
    	spinner: document.querySelector('.loader'),
    	addDialog: document.querySelector('.dialog-container')
  	};


  /*****************************************************************************
   *
   * Event listeners for UI elements
   *
   ****************************************************************************/

	document.getElementById('butRefresh').addEventListener('click', function() {
    	app.fetchLatestNews('all');
	});

	document.getElementById('butAdd').addEventListener('click', function() {
    	app.toggleAddDialog(true);
	});

	/*document.getElementById('butAddCity').addEventListener('click', function() {
    	app.toggleAddDialog(false);
    	app.spinner.setAttribute('hidden', false);
    	//var title = document.getElementById('title').value;
    	//var content = document.getElementById('content').value;
    	fetch('http://localhost:8888/PWA_DEMO/addNews.php', {
  			method: 'POST',
  			//mode: 'no-cors',
  			body: new FormData(document.getElementById('newsForm'))
		})
		.then((resp) => resp.json()) 
		.then(function(data) {
			let news = data.news;
			const ul = document.getElementById('newsList');
			ul.innerHTML = '';
			return news.map(function(ne) {
			  let li = createNode('li'),
				  spanTitle = createNode('div'),
				  spanContent = createNode('span');
			  spanTitle.innerHTML = ne.title;
			  spanContent.innerHTML = ne.content;
			  append(li, spanTitle);
			  append(li, spanContent);
			  append(ul, li);
			})
		})
		.catch(function(error) {
			app.spinner.setAttribute('hidden', true);
		});
  	});*/

  	document.getElementById('butAddCancel').addEventListener('click', function() {
  		app.toggleAddDialog(false);
  		
  	});
  
  	/********Demo 1 - Use Fetch API*******/
  	//App startup code here
  
	/*if (!('fetch' in window)) {
		console.log('Fetch API not found, try including the polyfill');
		return;
	}*/
	////Add Fetch API to get Remote result.
	
	/////Traditional XMLHttpRequest /////////
	/*var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
		   	app.spinner.setAttribute('hidden', true);
		   	var news = JSON.parse(xhttp.responseText).news;
			const ul = document.getElementById('newsList');
			return news.map(function(ne) {
			  let li = createNode('li'),
				  spanTitle = createNode('div'),
				  spanContent = createNode('span');
			  spanTitle.innerHTML = ne.title;
			  spanContent.innerHTML = ne.content;
			  append(li, spanTitle);
			  append(li, spanContent);
			  append(ul, li);
			})
		}
	};
	xhttp.open("GET", "news.json", true);
	xhttp.send();*/
	////////////////////////////////////////
	
	app.fetchLatestNews = function(city){
		app.spinner.setAttribute('hidden', false);
		
		fetch('news.json')
		.then((resp) => resp.json()) // Transform the data into json
		.then(function(data) {
			app.spinner.setAttribute('hidden', true);
			let news = data.news;
			const ul = document.getElementById('newsList');
			ul.innerHTML = '';
			return news.map(function(ne) {
			  let li = createNode('li'),
				  spanTitle = createNode('div'),
				  spanContent = createNode('span');
			  spanTitle.innerHTML = ne.title;
			  spanContent.innerHTML = ne.content;
			  append(li, spanTitle);
			  append(li, spanContent);
			  append(ul, li);
			})
		})
		.catch(function(error) {
			app.spinner.setAttribute('hidden', true);
			alert('Ooops!!! Looks like there was a problem');
		});
	}
	
	//app.fetchLatestNews('all');
	
	function createNode(element) {
    	return document.createElement(element); // Create the type of element you pass in the parameters
  	}

	function append(parent, el) {
    	return parent.appendChild(el); // Append the second parameter(element) to the first one
  	}
  	
  	app.toggleAddDialog = function(visible) {
    	if (visible) {
      		app.addDialog.classList.add('dialog-container--visible');
    	} else {
      		app.addDialog.classList.remove('dialog-container--visible');
    	}
  	};
	
	
	/********End of Demo 1*******/
	
	
	/********Demo 2 Caching Data*******/
	
	// TODO add service worker code here
  	if ('serviceWorker' in navigator) {
    	navigator.serviceWorker
             .register('./service-worker.js')
             .then(initialiseState);
  	}
	
	/********End of Demo 2*************/
	
	/********Demo 3 Working with Promises****/
	
	////////Old way of handling async code////
	
	/*setTimeout(function(){
		console.log('1');
		setTimeout(function(){
			console.log('2');
			setTimeout(function(){
				console.log('3');
				setTimeout(function(){
					console.log('4');
					setTimeout(function(){
						console.log('5');
					},1000);
				},1000);
			},1000);
		},1000);
	},1000)*/
	
	/*var p = new Promise(function(resolve, reject) {
	
		// Do an async task async task and then...

		if(app.promiseSuccess) {
			resolve('Success!');
		}
		else {
			reject('Failure!');
		}
	});

	p.then(function() { 
		console.log('Success');
	}).catch(function() {
		console.log('Error');
	});*/
	
	/*var request1 = fetch('ahmedabad_news.json');
	var request2 = fetch('gandhinagar_news.json');

	Promise.all([request1, request2]).then(function(results) {
		// Both promises done!
	});*/
	
	/*var req1 = new Promise(function(resolve, reject) { 
		setTimeout(function() { resolve('First!'); }, 4000);
	});
	
	var req2 = new Promise(function(resolve, reject) { 
		setTimeout(function() { reject('Second!'); }, 3000);
	});
	
	Promise.all([req1, req2]).then(function(results) {
		console.log('Then: ', results);
	}).catch(function(err) {
		console.log('Catch: ', err);
	});*/	
	
	/********End on Demo 3*******************/
	
	
	/********Demo 4 Working with IndexedDB*******/
	
	/*var indexedDB = window.indexedDB || window.mozIndexedDB || window.webkitIndexedDB || window.msIndexedDB || window.shimIndexedDB;

	// Open (or create) the database
	var open = indexedDB.open("NewsDatabase", 1);

	// Create the schema
	open.onupgradeneeded = function() {
		app.db = open.result;
		var store = app.db.createObjectStore("NewsStore", {keyPath: "id"});
	};

	open.onsuccess = function() {
		// Start a new transaction
		app.db = open.result;
		app.tx = app.db.transaction("NewsStore", "readwrite");
		app.store = app.tx.objectStore("NewsStore");
		
		/*Read all data
		var cursorRequest = app.store.openCursor();
		
		cursorRequest.onsuccess = function(evt) {                    
			var cursor = evt.target.result;
			if (cursor) {
				const ul = document.getElementById('newsList');
    	
    		let li = createNode('li'),
				  spanTitle = createNode('div'),
				  spanContent = createNode('span');
			  spanTitle.innerHTML = cursor.value.title;
			  spanContent.innerHTML = cursor.value.content;
			  append(li, spanTitle);
			  append(li, spanContent);
			  append(ul, li);
				cursor.continue();
			}
		};*/
		
	//}
    
    /*document.getElementById('butAddCity').addEventListener('click', function() {
    	app.toggleAddDialog(false);
    	var title = document.getElementById('title').value;
    	var content = document.getElementById('content').value;
    	
    	app.tx = app.db.transaction("NewsStore", "readwrite");
		app.store = app.tx.objectStore("NewsStore");
    	app.store.put({id: Date.now(), title: title, content: content});
    	
    	const ul = document.getElementById('newsList');
    	
    	let li = createNode('li'),
				  spanTitle = createNode('div'),
				  spanContent = createNode('span');
			  spanTitle.innerHTML = title;
			  spanContent.innerHTML = content;
			  append(li, spanTitle);
			  append(li, spanContent);
			  append(ul, li);
    });*/
    
	/********End of Demo 4***********************/
	
	/********Demo 5 Adding Push notifications*******/
	
	function initialiseState() {  
	  // Are Notifications supported in the service worker?  
	  if (!('showNotification' in ServiceWorkerRegistration.prototype)) {  
		console.warn('Notifications aren\'t supported.');  
		return;  
	  }

	  // Check the current Notification permission.  
	  // If its denied, it's a permanent block until the  
	  // user changes the permission  
	  if (Notification.permission === 'denied') {  
		console.warn('The user has blocked notifications.');  
		return;  
	  }

	  // Check if push messaging is supported  
	  if (!('PushManager' in window)) {  
		console.warn('Push messaging isn\'t supported.');  
		return;  
	  }

	  // We need the service worker registration to check for a subscription  
	  navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {  
		// Do we already have a push message subscription?  
		serviceWorkerRegistration.pushManager.getSubscription()  
		  .then(function(subscription) {  
			var pushButton = document.querySelector('.js-push-button');  
			pushButton.disabled = false;
			pushButton.onclick = subscribe;
			if (!subscription) {  
			  return;  
			}
			console.log(subscription);
			pushButton.textContent = 'Disable Push Messages';  
		  })  
		  .catch(function(err) {  
			console.warn('Error during getSubscription()', err);  
		  });  
	  });  
	}
	
	function subscribe() {  
		  var pushButton = document.querySelector('.js-push-button');  
		  pushButton.disabled = true;

		  navigator.serviceWorker.ready.then(function(serviceWorkerRegistration) {  
			serviceWorkerRegistration.pushManager.subscribe({userVisibleOnly: true})  
			  .then(function(subscription) {  
				// The subscription was successful 
				console.log(subscription); 
				pushButton.textContent = 'Disable Push Messages';  
				pushButton.disabled = false;
			  })  
			  .catch(function(e) {  
				if (Notification.permission === 'denied') {  
				  console.warn('Permission for Notifications was denied');  
				  pushButton.disabled = true;  
				} else {  
				  console.error('Unable to subscribe to push.', e);  
				  pushButton.disabled = false;  
				  pushButton.textContent = 'Enable Push Messages';  
				}  
			  });  
		  });  
		}
	
	
	/********End of Demo 5***********************/
	
})();
