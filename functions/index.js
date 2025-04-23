
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.helloCaroline = functions.https.onRequest((req, res) => {
  res.status(200).send("Hello Caroline, your cloud function is live and running!");
});
