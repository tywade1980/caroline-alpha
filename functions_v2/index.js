
const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

exports.helloCaroline = functions.https.onRequest((req, res) => {
  res.status(200).send("Hello Caroline, your cloud function is live and running!");
});

exports.testCommand = functions.https.onRequest((req, res) => {
  const command = req.query.cmd || "none";
  res.status(200).send(`Command received: ${command}`);
});
