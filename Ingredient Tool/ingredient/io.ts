import * as nodeFs from 'fs'
import * as Promise from 'bluebird'
import {IngredientGraph} from './graph'
const fs: any = Promise.promisifyAll(nodeFs);


/**
 * Read an ingredient graph from a file
 */
export function readIngredientGraphAsync(filename: string) {
    return fs.readFileAsync(filename, 'utf8')
    .then(file => {
        let parsedFile = JSON.parse(file);
        //TODO actually do this
        return new IngredientGraph();
    })
    .catch(err => {
        console.error(`Error reading ingredient file ${filename}`);
        throw err;
    });
}


/**
 * Write an ingredient graph to a file
 */
export function writeIngredientGraphAsync(graph: IngredientGraph, filename: string) : Promise<void> {
    //TODO actually do this
    let serializedGraph = "{}";
    return fs.writeFileAsync(filename, serializedGraph);
}
