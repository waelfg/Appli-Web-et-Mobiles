let express = require('express');
let router = express.Router();
let service = require('../services/requests.js');

router.get('/getmsg', service.retrieveMessages);

router.post('/login',service.connectUser);
router.post('/signup', service.signupUser);
router.post('/sendmsg', service.sendMessage);
router.post('/contacts', service.synchronizeContacts);



module.exports = router;