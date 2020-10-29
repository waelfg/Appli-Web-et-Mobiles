let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/AppliWeb', {
    useNewUrlParser: true,
    useUnifiedTopology: true
}, function (err) {
    if (err) console.log("Connection to database error : " + err + "\n");
    else console.log("Connected to database successfully.\n")
})

let UserSchema = new mongoose.Schema({
    numTel: String,
    password: String,
    name: String,
    contacts: [String],
    location: {
        type: {
            type: String,
            enum: ['Point']
        },
        coordinates: {
            type: [Number]
        }
    }
});

UserSchema.index({ location: "2dsphere" });

let MessageSchema = new mongoose.Schema({
    from: String,
    to: String,
    location: {
        type: {
            type: String,
            enum: ['Point']
        },
        coordinates: {
            type: [Number]
        }
    },
    date: Date,
    body: String,
    issued: Boolean
});

MessageSchema.index({ location: "2dsphere" });

let User = mongoose.model('Users', UserSchema);
let Message = mongoose.model('Messages', MessageSchema);

module.exports = {
    mongoose,
    User,
    Message
}