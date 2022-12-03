FROM node:16 AS superset-node

ARG NPM_VER=7
RUN npm install -g npm@${NPM_VER}

ARG NPM_BUILD_CMD="build"
ENV BUILD_CMD=${NPM_BUILD_CMD}

# NPM ci first, as to NOT invalidate previous steps except for when package.json changes
RUN mkdir -p /app/superset-frontend
RUN mkdir -p /app/superset/assets
COPY ./docker/frontend-mem-nag.sh /
COPY ./superset-frontend /app/superset-frontend
RUN npm config set registry https://registry.npmmirror.com
RUN /frontend-mem-nag.sh \
        && cd /app/superset-frontend \
	&& npm ci

# This seems to be the most expensive step
RUN cd /app/superset-frontend \
        && npm run ${BUILD_CMD} \
        && rm -rf node_modules

ARG PY_VER=3.8.12
FROM wujt114655/superset-py:2.0.1

ENV LANG=C.UTF-8 \
    LC_ALL=C.UTF-8 \
    FLASK_ENV=development \
    FLASK_APP="superset.app:create_app()" \
    PYTHONPATH="/app/pythonpath" \
    SUPERSET_HOME="/app/superset_home" \
    SUPERSET_CONFIG_PATH=/app/superset/conf/superset_config.py \
    SUPERSET_PORT=3000

RUN mkdir -p ${PYTHONPATH} \
	&& mkdir -p ${SUPERSET_HOME}
#        && useradd --user-group -d ${SUPERSET_HOME} -m --no-log-init --shell /bin/bash superset

# COPY --from=wujt114655/superset-py:2.0 /usr/local/lib/python3.8/site-packages/ /usr/local/lib/python3.8/site-packages/
# Copying site-packages doesn't move the CLIs, so let's copy them one by one
RUN cp /usr/local/bin/gunicorn /usr/local/bin/celery /usr/local/bin/flask /usr/bin/
COPY --from=superset-node /app/superset/static/assets /app/superset/static/assets
COPY --from=superset-node /app/superset-frontend /app/superset-frontend

## Lastly, let's install superset itself
COPY superset /app/superset
COPY setup.py MANIFEST.in README.md /app/
RUN cd /app \
#        && chown -R superset:superset * \
        && pip install -e . \
        && flask fab babel-compile --target superset/translations

COPY ./docker/run-server.sh /usr/bin/

RUN chmod a+x /usr/bin/run-server.sh

WORKDIR /app

#USER superset

HEALTHCHECK CMD curl -f "http://localhost:$SUPERSET_PORT/health"

EXPOSE ${SUPERSET_PORT}

CMD /usr/bin/run-server.sh
