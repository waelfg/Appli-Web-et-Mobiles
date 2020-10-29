let express = require('express')
let app = express();
let database = require('./db/mongo.js').mongoose;
let cookieParser = require('cookie-parser');
//Creating the secured server using ssl keys.
let fs = require('fs');
let server = require('https').createServer({
    key: fs.readFileSync('./ssl/key.pem'),
    cert: fs.readFileSync('./ssl/cert.pem')
}, app);

let session = require('express-session')
let bodyParser = require('body-parser')  // envoie des paramètres en POST
let io = require('socket.io')(server);
let router = require('./routes/routes');

//Setting the server parameters : session (cookie), parser, router, etc.
app.use(cookieParser());
app.use(session({
    secret: 'shhhhhhhhhhhhh',
    resave: false,
    saveUninitialized: true,
    secure: true
}));
// pour gérer les URL-encoded bodies (envoie formulaire en POST)
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended:false}));
app.use('',router);
io.on('connect', function (socket) {
    console.log("SOCKET" + socket);
});

//Handle the good connection to the database
database.connection.on('error', console.error.bind(console, 'connection error:'));
database.connection.once('open', function () {

    app.listen(3000);
    console.log("Server listening on port 3000...")

});


process.on('SIGINT', function() {
    database.connection.close(function () {
        console.log('Mongoose default connection disconnected through app termination');
        process.exit(0);
    });
});
