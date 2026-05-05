#!/usr/bin/env python3
"""Centauri Interlock Node for Manus API v2.

This node wraps Manus API calls so repositories can create Manus tasks without
breaking the Interlock Standard. It reads `caroline_neuro_memory.json` before
execution and emits a structured JSON broadcast for Command_Router ingestion.

Environment variables:
    MANUS_API_KEY       Required for live Manus API calls.
    MANUS_API_BASE_URL  Optional; defaults to https://api.manus.ai.

Input can be supplied either as stdin JSON or command-line JSON. Minimal input:
    {"action": "create_task", "prompt": "Do the thing", "title": "Task"}
"""
from __future__ import annotations

import json
import os
import sys
import urllib.error
import urllib.request
from datetime import datetime, timezone
from pathlib import Path
from typing import Any, Dict, Optional

NODE_ID = "manus_api_node"
BASE_DIR = Path(__file__).resolve().parents[1] if Path(__file__).resolve().parent.name in {"nodes", "scripts"} else Path.cwd()
STATE_FILE = Path(os.getenv("CAROLINE_STATE_FILE", str(BASE_DIR / "caroline_neuro_memory.json")))
MANUS_API_BASE_URL = os.getenv("MANUS_API_BASE_URL", "https://api.manus.ai").rstrip("/")


def now_iso() -> str:
    return datetime.now(timezone.utc).isoformat()


def read_system_state() -> Dict[str, Any]:
    if STATE_FILE.exists():
        return json.loads(STATE_FILE.read_text())
    return {
        "_schema_version": "2.0.0",
        "system": {"status": "initialized"},
        "neurorank": {"composite_score": 0.0, "regions": {}},
        "context": {},
        "integrations": {},
        "history": [],
    }


def broadcast_result(status: str, payload: Optional[Dict[str, Any]] = None, error: Optional[str] = None) -> Dict[str, Any]:
    result = {
        "node_id": NODE_ID,
        "timestamp": now_iso(),
        "status": status,
        "payload": payload or {},
        "error": error,
    }
    print(json.dumps(result, indent=2))
    return result


def read_input() -> Dict[str, Any]:
    raw = ""
    if not sys.stdin.isatty():
        raw = sys.stdin.read().strip()
    if not raw and len(sys.argv) > 1:
        raw = " ".join(sys.argv[1:]).strip()
    if not raw:
        return {"action": "health"}
    return json.loads(raw)


def manus_request(endpoint: str, method: str = "POST", body: Optional[Dict[str, Any]] = None) -> Dict[str, Any]:
    api_key = os.getenv("MANUS_API_KEY")
    if not api_key:
        raise RuntimeError("MANUS_API_KEY is not set; refusing to make a live Manus API call.")
    data = None if body is None else json.dumps(body).encode("utf-8")
    request = urllib.request.Request(
        f"{MANUS_API_BASE_URL}/v2/{endpoint.lstrip('/')}",
        data=data,
        method=method,
        headers={
            "content-type": "application/json",
            "x-manus-api-key": api_key,
        },
    )
    try:
        with urllib.request.urlopen(request, timeout=60) as response:
            return json.loads(response.read().decode("utf-8"))
    except urllib.error.HTTPError as exc:
        detail = exc.read().decode("utf-8", errors="replace")
        raise RuntimeError(f"Manus API HTTP {exc.code}: {detail}") from exc


def create_task(command: Dict[str, Any], state: Dict[str, Any]) -> Dict[str, Any]:
    prompt = command.get("prompt") or command.get("message") or state.get("context", {}).get("active_query")
    if not prompt:
        raise ValueError("create_task requires `prompt`, `message`, or context.active_query in state.")
    content = [{"type": "text", "text": str(prompt)}]
    for file_id in command.get("file_ids", []):
        content.append({"type": "file", "file_id": file_id})
    body: Dict[str, Any] = {
        "message": {
            "content": content,
        },
        "title": command.get("title", "Centauri OS Manus Task"),
    }
    connectors = command.get("connectors") or state.get("integrations", {}).get("manus", {}).get("connectors")
    if connectors:
        body["message"]["connectors"] = connectors
    project_id = command.get("project_id") or state.get("integrations", {}).get("manus", {}).get("project_id")
    if project_id:
        body["project_id"] = project_id
    response = manus_request("task.create", "POST", body)
    return {"request": body, "response": response}


def list_messages(command: Dict[str, Any]) -> Dict[str, Any]:
    task_id = command.get("task_id")
    if not task_id:
        raise ValueError("list_messages requires task_id.")
    limit = int(command.get("limit", 50))
    order = command.get("order", "asc")
    return manus_request(f"task.listMessages?task_id={task_id}&limit={limit}&order={order}", "GET")


def main() -> None:
    try:
        state = read_system_state()
        command = read_input()
        action = command.get("action", "health")
        priority = state.get("neurorank", {}).get("composite_score", 0.0)
        if action == "health":
            payload = {
                "integration": "manus_api",
                "status": "ready",
                "state_file": str(STATE_FILE),
                "has_api_key": bool(os.getenv("MANUS_API_KEY")),
                "neurorank_priority": priority,
            }
        elif action == "create_task":
            payload = {
                "integration": "manus_api",
                "action": action,
                "neurorank_priority": priority,
                **create_task(command, state),
            }
        elif action == "list_messages":
            payload = {
                "integration": "manus_api",
                "action": action,
                "neurorank_priority": priority,
                "response": list_messages(command),
            }
        else:
            raise ValueError(f"Unsupported action: {action}")
        broadcast_result("SUCCESS", payload=payload)
    except Exception as exc:
        broadcast_result("ERROR", error=str(exc))


if __name__ == "__main__":
    main()
