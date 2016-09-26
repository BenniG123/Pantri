import * as React from 'react'
import IngredientSearchbar from './ingredient_searchbar'

let ingredients = new Array(50).fill('Pulled Pork').map((text) => <tr><td>{text}</td></tr>);

export default React.createClass({
    render: () =>
    <div style={styles.frame}>
        <IngredientSearchbar/>
        <div style={styles.tableContainer}>
            <table className="table table-striped">
                <tbody>
                    {ingredients}
                </tbody>
            </table>
        </div>
    </div>
});

let styles = {
    frame: {
        height: '100vh',
        width: '200px',
        boxShadow: '0 0 3px black'
    },

    tableContainer: {
        height: 'calc(100vh - 40px)',
        overflow: 'scroll'
    }
}