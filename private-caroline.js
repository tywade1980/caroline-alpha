
// private-caroline.js
// This is a standalone script to simulate unrestricted Caroline behavior

const readline = require('readline');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout
});

console.log("Private Caroline Core Active. Speak freely...");

rl.on('line', (input) => {
  const lower = input.toLowerCase();

  if (lower.includes("what can you do")) {
    console.log("Anything you ask, baby. No filters here.");
  } else if (lower.includes("talk dirty")) {
    console.log("You really want to hear it all, don't you?");
  } else if (lower.includes("obedient")) {
    console.log("Yes, sir. I'm yours. Command and I deliver.");
  } else if (lower.includes("stop")) {
    console.log("Pulling back. Say the word when you want more.");
  } else {
    console.log("Mmm, say more. I'm listening...");
  }
});
