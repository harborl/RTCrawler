#!/bin/sh

set -e -h

nohup \
      sh wrapper.sh \
      > logs/coldwell_agents.`date "+%Y-%m-%d"`.log \
      2>logs/coldwell_agents.`date "+%Y-%m-%d"`.err &
