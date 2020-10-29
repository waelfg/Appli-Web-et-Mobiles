let mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/AppliWeb', {
    useNewUrlParser: true,
    useUnifiedTopology: true
}, function (err) {
    if (err) console.log("Connection to database error : " + err + "\n");
    else console.log("Connected to database successfully.\nSetting up database ...")
});
mongoose.connection.once('open', async function () {
    let UserSchema = new mongoose.Schema({
        numTel: String,
        password: String,
        name: String,
        contacts: [String],
        location: {
            type: {
                type: String, // Don't do `{ location: { type: String } }`
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
                type: String, // Don't do `{ location: { type: String } }`
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

    if (await User.countDocuments() === 0) {
        console.log("0 users");
        let admin = new User({
            name: "Administrateur",
            numTel: "0606060606",
            password: "admin",
            contacts: ["0505050505", "0303030303"],
            location: {
                type: 'Point',
                coordinates: [0.0,0.0]
            }
        });
        await admin.save(function (err, user) {
            if (err) throw err;
            console.log(user);

        });
    }

    if (await Message.countDocuments() === 0) {
        console.log("0 messages");
        let preview = new Message({
            from: "0606060606",
            to: "0606060606",
            location: {
                type: 'Point',
                coordinates: [0,0]
            },
            date: new Date(),
            body: "HEllo, this is a message preview",
            issued: true
        })
        await preview.save(function (err, message) {
            if (err) throw err;
            console.log(message);
        });
    }

    console.log("Setup is finished. Enjoy !");


    let user = {
        numTel: "0606060606"
    }

    let coords = {
        location: {
            type:'Point',
            coordinates: [0.0058,-0.003]
        }
    }
    await User.updateOne(user, coords);

    let longitude = 0.0;
    let latitude = 0.0;
    let contacts=["0606060606","0505050505"]
    let search = {
        location: {
            $near: {
                $maxDistance: 730,//Distance in meters
                $geometry: {
                    type: "Point",
                    coordinates: [longitude, latitude]
                }
            }
        },
        numTel: {
            $in: contacts
        }
    };
    await User.find(search,{'_id':0}, function (error, resultat) {
        if (error) throw error;
        resultat.forEach(function (elem) {
            console.log("Trouvé : "+ elem);
        })
    });

});


process.on('SIGINT', function () {
    mongoose.connection.close(function () {
        console.log('Mongoose default connection disconnected through app termination');
        process.exit(0);
    });
});

