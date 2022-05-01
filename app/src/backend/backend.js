//var http = require("http")
//var url = require('url');
//const express = require('express')
//const Server = express();
//const PORT = 8080;
//const BASEURL = "10.0.2.2:8080"
//let login_request_count = 0
//
////Login
//Server.get('', (req, res) => {
//    console.log('\x1b[36m%s\x1b[0m',"###REQUEST START###")
//    console.log(req.originalUrl, req.query.msg);
//    console.log("Request Count: ", ++login_request_count);
//    console.log('\x1b[36m%s\x1b[0m',"###REQUEST END###")
//});
//
//console.clear();
//
////Listen on PORT
//Server.listen(PORT, () => console.log(`Server is online at localhost:${PORT}`))

var net = require('net');
var host = 'localhost';
var port = 8080;
var socket = new net.Socket();

socket.connect(port, host, () => {

});