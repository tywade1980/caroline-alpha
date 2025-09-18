// caroline-core.js
module.exports = {
  enforceFollowThrough(task) {
    if (!task.completed) {
      return `Reminder: '${task.name}' is still pending.`;
    }
    return `Task '${task.name}' is complete.`;
  },
  reinforce(actionResult) {
    const weight = actionResult.importance;
    const outcome = actionResult.success ? 1 : -1;
    return weight * outcome;
  },
  interpretCommand(command) {
    const map = {
      "create estimate": "estimates.create()",
      "add client": "crm.addClient()",
      "start project": "projects.newProject()",
      "send invoice": "finance.sendInvoice()"
    };
    return map[command.toLowerCase()] || "command.notRecognized()";
  },
  isRevenueGenerating(action) {
    const keywords = ["install", "build", "tile", "paint", "deliver"];
    return keywords.some(k => action.name.toLowerCase().includes(k));
  }
};
