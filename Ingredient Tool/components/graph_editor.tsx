import * as React from 'react'
import {remote} from 'electron'
import {IngredientGraph} from '../ingredient/graph'

/**
 * Properties expected by the graph editor
 */
interface EditorProps {
    graph: IngredientGraph
}


export default React.createClass<EditorProps, any>({
    render: () =>
    <h1>Testing</h1>,

    componentDidMount: () => {
        let window = remote.getCurrentWindow();
        window.setSize(850, 650);
        window.setTitle('Edit Ingredients');
        window.setMaximizable(true);
        window.setResizable(true);
    }
});
