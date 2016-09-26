import * as React from 'react'

export default React.createClass({
    render: () =>
    <div style={styles.frame}>
        <input className="form-control" type="text" placeholder="Search for an ingredient" style={styles.searchbar}/>
    </div>
})

let styles = {
    frame: {
        padding: '5px',
        height: '40px',
        boxSizing: 'border-box',
        borderBottom: '1px solid #222'
    },

    searchbar: {
        backgroundColor: '#1f1f1f',
        height: '30px',
        borderRadius: '5px',
        boxShadow: 'inset 0 0 3px black',
        color: '#eee'
    }
}