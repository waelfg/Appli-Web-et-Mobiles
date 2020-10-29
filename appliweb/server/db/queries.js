let mongo = require('./mongo.js');

async function findUsersClose(params, callback) {
    await mongo.User.find(params, callback);
}

async function findOneUser(params, callback) {
    await mongo.User.findOne(params, callback);
}

async function addOneUser(params, callback) {
    let user = new mongo.User({
        name: params.name,
        numTel: params.numTel,
        password: params.password,
        contacts: [],
        location:{
            type:'Point',
            coordinates: [0,0]
        }
    });
    await user.save(callback);
}

async function userExists(params, callback) {
    let object = {numTel:params.numTel};
    await mongo.User.find(object, callback);
}

async function sendMessage(params, callback) {
    let message = new mongo.Message({
        from: params.from,
        to: params.to,
        location:{
          type:'Point',
          coordinates: [params.longitude,params.latitude]
        },
        date: new Date() ,
        body: params.body,
        issued: false
    });
    await message.save(callback);
}

async function getMessages(params, callback){

    await mongo.Message.updateMany({to:params.user},{issued: true},function (error,resultat) {
        if (error) throw error;
        else {
            console.log("UPDATE MESSAGE : "+JSON.stringify(resultat));
        }
    });

    let search = {to: params.user, issued: true};
    mongo.Message.find(search, callback);
}

async function deleteOldMessages(){
    let yesterday = new Date();
    yesterday.setDate(yesterday.getDate() - 1);
    mongo.Message.deleteMany({issued: false, date: { $lte: yesterday}});
}

async function updateCoordsUser(params, callback){
    let user = {
        numTel: params.numTel
    }

    let coords = {
        location: {
            type:'Point',
            coordinates: [params.longitude,params.latitude]
        }
    }
    mongo.User.updateOne(user, coords, callback);
}

async function updateContactsUser(params, callback) {
    let user = {
        numTel: params.numTel
    }
    let contacts = {
        contacts: params.contacts
    };
    mongo.User.updateOne(user,contacts,callback)
}
module.exports = {
    findUsersClose,
    findOneUser,
    addOneUser,
    userExists,
    sendMessage,
    getMessages,
    updateCoordsUser,
    updateContactsUser,
    deleteOldMessages
}