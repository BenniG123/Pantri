import * as React from 'react'
import {remote} from 'electron'
import * as os from 'os'
import {IngredientGraph} from '../ingredient/graph'
import * as IngredientIO from '../ingredient/io'
import * as MainWindow from '../renderer/main'
import GraphEditor from './graph_editor'


export default React.createClass({
    render: () =>
        <div style={styles.frame}>
            <p>
                Would you like to open an existing ingredient file or create a new one?
            </p>
            <div id="buttons" style={styles.buttons}>
                <button style={styles.button} className="btn btn-default" onClick={openFile}>Open</button>
                <button style={styles.button} className="btn btn-default" onClick={createFile}>Create</button>
            </div>
        </div>,
    
    componentDidMount: () => {
        let window = remote.getCurrentWindow();
        window.setSize(400, 150);
        window.setTitle('Open or Create an Ingredients');
        window.setMaximizable(false);
        window.setResizable(false);
        window.webContents.openDevTools();
    }

});


/**
 * Styles for the dialog
 */
let styles = {
    frame: {
        margin: '20px'
    },
    buttons: {
        position: 'absolute',
        bottom: '15px',
        right: '20px' 
    }, 
    button: {
        margin: '2px'
    }
}


/**
 * Trigger a dialog to open an ingredient file
 */
function openFile() {
    remote.dialog.showOpenDialog({
        title: 'Choose an Ingredient File',
        filters: [{name: 'Ingredient Database', extensions: ['idb']}],
        properties: ['openFile']
    }, (filenames) => {
        // This will be null if the user cancels
        if (!filenames)
            return;

        // We restrict the user to a single file    
        IngredientIO.readIngredientGraphAsync(filenames[0])
        .then(graph => MainWindow.setWindowComponent(<GraphEditor graph={graph}/>))
    });
}


/**
 * Trigger a dialog to create an ingredient file
 */
function createFile() {
    remote.dialog.showSaveDialog({
        title: 'Save Your Ingredient File',
        filters: [{name: 'Ingredient Database', extensions: ['idb']}],
        defaultPath: `${os.homedir}/ingredients.idb`
    }, (filename) => {
        if (!filename)
            return;
        
        let graph = new IngredientGraph();
        IngredientIO.writeIngredientGraphAsync(graph, filename)
        .then(() => MainWindow.setWindowComponent(<GraphEditor graph={graph}/>));
    });
}
