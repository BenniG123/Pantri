import {app, BrowserWindow} from 'electron'
import * as path from 'path';


// Start up everything
app.on('ready', () => {
    let window = new BrowserWindow({show: false});
    window.on('closed', () => app.quit());

    let filePath = path.normalize(`${__dirname}/../renderer/window.html`);
    console.log(filePath);
    window.loadURL(`file://${filePath}`);
})
