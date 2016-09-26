import * as React from 'react'

export default React.createClass({
    render: () =><div style={styles.frame}></div>
});

let styles = {
    frame: {
        height: '100vh',
        backgroundColor: 'red',
        width: '200px'
    }
}