#~ for i in 0 50 100 150 200 400 500 750 1000
#~ do
#~ echo -e "java -jar jar/chess_queens_ts_g2n4.jar -n 200 -t $i --nRuns 100 --nExec 100 --plots"
#~ java -jar jar/chess_queens_ts_g2n4.jar -n 200 -t $i --nRuns 100 --nExec 100 --plots
#~ done

java -jar jar/chess_queens_ts_g1n1.jar -n 100 -t 200 --nRuns 100 --nExec 100 --plots -v
java -jar jar/chess_queens_ts_g2n1.jar -n 100 -t 200 --nRuns 100 --nExec 100 --plots -v
