//convert ZNR.png -monochrome -r esize "84x48>" result.bmp
//convert -size 84x48 xc:white ZNR.bmp -gravity center -composite file

"use strict"

const fs = require('fs');
var execSync = require('child_process').execSync;

let files = fs.readdirSync("./files");

for (let file of files) {
	const name = file.split(".png")[0].replace(" ", "\ ");
	let extension = "png";

	//const convert = `convert "./files/${name}.${extension}" -monochrome -resize "84x48>" "./images/${name}.bmp"`;
	const convert = `convert "./files/${name}.${extension}" -filter point -resize "84x48>" "./images/${name}.bmp"`;
	const resize = `convert -size 84x48 xc:white "./images/${name}.bmp" -gravity center -composite "./images/${name}.bmp"`;

	console.log(convert);
	execSync(convert);

	console.log(resize);
	execSync(resize);
}