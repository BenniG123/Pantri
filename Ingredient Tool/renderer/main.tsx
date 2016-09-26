import {remote} from 'electron'
import * as React from 'React'
import * as ReactDom from 'react-dom'
import StartDialog from '../components/start_dialog'

window.onload = () => {
    ReactDom.render(<StartDialog/>, document.getElementById('content'));
    remote.getCurrentWindow().setMenu(null);
    remote.getCurrentWindow().show();
};


/**
 * Set the main component housed inside the window
 */
export function setWindowComponent(component: React.ReactElement<any>) {
    ReactDom.render(component, document.getElementById('content'));
}
