FROM oracle/graalvm-ce:19.2.0.1 as graalvm
COPY . /home/app/rental-calculator
WORKDIR /home/app/rental-calculator
RUN gu install native-image
RUN native-image --no-server -cp build/libs/rental-calculator-*-all.jar

FROM frolvlad/alpine-glibc
COPY --from=graalvm /home/app/rental-calculator .
ENTRYPOINT ["./rental-calculator"]
