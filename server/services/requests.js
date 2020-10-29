let mongo = require('../db/queries.js');


async function findUsersClose(params, callback) {
    let contacts = params.contacts;
    let longitude = params.longitude;
    let latitude = params.latitude;

    let search = {
        location: {
            $near: {
                $maxDistance: 10000,//Distance in meters here, 10 kilometers
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

    await mongo.findUsersClose(search, callback);
}

async function retrieveMessages(req, res) {
    let messages = [];
    let numTel = req.session.user;

    await mongo.deleteOldMessages();

    await mongo.getMessages({user: numTel}, function (error, resultat) {
        if (error) throw error;
        resultat.forEach(function (elem) {
            messages.push({longitude: elem.location.coordinates[0], latitude:elem.location.coordinates[0], from:elem.from,to:elem.to,date:elem.date,body:elem.body});
        });
        res.status(200).send(JSON.stringify({messages: messages}));
    });
}

async function connectUser(req, res) {
    let numTel = req.body.numTel;
    let password = req.body.password;
    let latitude = req.body.latPos;
    let longitude = req.body.longPos;
    let contacts = req.body.contacts;
    //Call the database to check if user with numTel exist.
    await mongo.findOneUser({numTel: numTel}, async function (error, resultat) {
        if (error) throw error;
        else {
            if (resultat) {
                //Check if the password are ok.
                if (resultat.password === password) {
                    req.session.user = numTel;
                    req.session.name = resultat.name;
                    req.session.longitude = longitude;
                    req.session.latitude = latitude;
                    req.session.contacts = contacts;
                    await mongo.updateCoordsUser({
                        numTel: numTel,
                        longitude: longitude,
                        latitude: latitude
                    }, function (error) {
                        if (error) throw error;
                        else {
                            console.log("User : " + numTel + " coordinates updated to : " + longitude + ", " + latitude);
                        }
                    });

                    await mongo.updateContactsUser({
                        numTel: numTel,
                        contacts: req.session.contacts
                    }, function (error) {
                        if (error) throw error;
                        else {
                            console.log("User : " + numTel + " contacts updated.");
                            res.status(200).json({name: req.session.name, numTel: req.session.user});


                        }
                    });

                } else {
                    res.status(401).send("Error, wrong password.")
                }
            } else {
                res.status(401).send("Error, you are not registered. Please SignUp")
            }
        }
    });
}


async function signupUser(req, res) {
    let numTel = req.body.numTel;
    let password = req.body.password;
    let name = req.body.name;
    let exist = false;
    await mongo.userExists({numTel: numTel}, function (error, resultat) {
        if (error) throw error;
        else {
            if (resultat.length > 0) exist = true;
        }
    });
    if (!exist) {
        await mongo.addOneUser({numTel: numTel, name: name, password: password}, function (error) {
            if (error) throw error;
            else {
                res.status(200).send("Registration complete, you can now sign-in.")
            }
        });
    } else {
        res.status(409).send("An account with this phone number is already registered.")
    }
}

async function sendMessage(req, res) {
    req.session.longitude = req.body.longPos;
    req.session.latitude = req.body.latPos;
    await mongo.updateCoordsUser({
        numTel: req.session.user,
        longitude: req.session.longitude,
        latitude: req.session.latitude
    }, function (error) {
        if (error) throw error;
        else {
            console.log("User : " + req.body.numSender + " coordinates updated to : " + req.body.longPos + ", " + req.body.latPos);
        }
    });

    let message = {
        from: req.session.user,
        latitude: req.session.latitude,
        longitude: req.session.longitude,
        body: req.body.body
    }

    let usersClose = [];

    await findUsersClose({
        longitude: req.session.longitude,
        latitude: req.session.latitude,
        contacts: req.session.contacts
    }, function (error, resultat) {
        //do stuff with err and res.
        if (error) throw error;
        resultat.forEach(function (elem) {
            usersClose.push(elem);
        });
    });

    if (usersClose && usersClose.length > 0)
        usersClose.forEach(function (user) {
            message.to = user.numTel;
            mongo.sendMessage(message, function (error) {
                if (error) throw error;
                else {
                    res.status(200).send("Message sent.");
                }
            });
        });
    else {
        res.status(500).send("Error, null null");
    }
}

async function synchronizeContacts(req, res) {
    req.session.contacts = req.body.contacts;

    await mongo.updateContactsUser({
        numTel: req.session.user,
        contacts: req.session.contacts
    }, function (error) {
        if (error) throw error;
        else {
            console.log("User : " + numTel + " contacts updated.");
            res.status(200).send("Contacts mis à jour !");
        }
    });
}

async function getMessages(req, res) {
    let messages = [];
    let params = {user: req.session.user};
    await mongo.getMessages(params, function (error, resultat) {
        if (error) throw error;
        resultat.forEach(function (elem) {
            messages.push(elem);
        });
        res.status(200).json({messages: messages});
    });
}

module.exports = {
    retrieveMessages, connectUser, signupUser, sendMessage, synchronizeContacts, getMessages
}