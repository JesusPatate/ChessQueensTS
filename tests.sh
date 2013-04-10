for i in 0 50 100 150 200 400 500
do
    echo -e "java -jar jar/chess_queens_ts.jar -n 200 -t $i --nRuns 100 --nExec 100 --plots"
    java -jar jar/chess_queens_ts.jar -n 200 -t $i --nRuns 100 --nExec 100 --plots
done
