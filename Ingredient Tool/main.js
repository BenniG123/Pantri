const {app, BrowserWindow} = require('electron');
const fs = require('fs');

const page = require('./view_util/page')

/** The window hsoting everything */
let globalWindow;


/**
 * Loads a page into the global window
 * @param {string} page the name of the page to loadPage
 * @param {Object} pageArgs any arguments the page should be given
 * 
 * @throws if a page of the name is not found, the page is invalid, or 
 */
function loadPageAsync(page, pageArgs = {}) {

}

// Start up everything
app.on('ready', () => {
    page.loadPagesAsync()
    
    .then(pageTable => {
        globalWindow = new BrowserWindow({width: 400, height: 150}); 
    })
})
