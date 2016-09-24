var Promise = require('bluebird');
const fs = require('fs');
const path = require('path');
Promise.promisifyAll(fs);

const HTML_NAME = 'page.html';
const STYLE_NAME = 'style.css';
const JS_NAME = 'logic.js';
const PAGES_DIRECTORY = path.normalize(`${__dirname}/../pages`);


/**
 * Data relevant to a page
 * @constructor
 */
function PageData(pageName, pageHtml, pageLogic, pageStyle) {
    this.pageName = pageName;
    this.pageHtml = pageHtml;
    this.pageLogic = pageLogic;
    this.pageStyle = pageStyle;
}
exports.PageData = PageData;


/**
 * Loads all pages into memory
 * 
 * @return {Object} mapping of page name to PageData
 */
exports.loadPagesAsync = () => {
    return fs.readdirAsync(PAGES_DIRECTORY)
    .then(pages => Promise.all(pages.map(loadPageDataAsync)))
    .then(loadedPages => {
        let pageTable = {}
        loadedPages.forEach(pageData => {
            pageTable[pageData.pageName] = pageData;
        });

        return pageTable;
    })
    .catch(err => {
        throw err;
    })
}


/**
 * Load all resources from a page into memory
 * 
 * @param {string} pageName the name of the page to be loaded
 * @return {PageData} the loaded page data
 */
function loadPageDataAsync(pageName) {
    let pageDirectory = `${PAGES_DIRECTORY}/${pageName}`;
    return fs.readdirAsync(pageDirectory)
    
    .catch(err => {
        console.error(`Couldn't read ${pageDirectory} directory, is this a file instead?`);
        throw err;
    })
    
    .then(files => {
        if (!files.includes(HTML_NAME))
            throw new Error(`${pageDirectory} contains no ${HTML_NAME} file`);
         if (!files.includes(JS_NAME))
            throw new Error(`${pageDirectory} contains no ${JS_NAME} file`);

        // We don't always have a style.css, only read it if we have one
        let filesToRead = [HTML_NAME];
        if (files.includes(STYLE_NAME))
            filesToRead.push(STYLE_NAME);

        return Promise.all(filesToRead.map((fileName) => {
            return fs.readFileAsync(`${pageDirectory}/${fileName}`);
        }));
    })

    .then(readFiles => {
        // This ordering is completely dependant on the lines above
        let pageHtml = readFiles[0];
        let pageStyle = readFiles[1] || "";
        let pageLogic = require(`${pageDirectory}/logic.js`);
        
        return new PageData(pageName, pageHtml, pageLogic, pageStyle);
    })

    .catch(err => {
        console.error(`Problem loading ${pageName} page`);
        throw err;
    })
}
