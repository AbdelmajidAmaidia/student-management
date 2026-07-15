#!/bin/bash

echo "Rollback started..."

kubectl rollout undo deployment/student-management

echo "Rollback completed."
