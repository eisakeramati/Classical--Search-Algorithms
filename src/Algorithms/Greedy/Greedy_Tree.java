package Algorithms.Greedy;

import Algorithms.Algorithm;
import Problems.CityPathProblem;
import Problems.Problem;

import java.util.ArrayList;

/**
 * Created by eisak on 2019-01-22.
 */
public class Greedy_Tree extends Algorithm{

    ArrayList<Problem.State> openStates;
    long start, end;

    public Greedy_Tree(Problem problem) {
        super(problem);
        openStates = new ArrayList<>();
        run();
    }

    @Override
    public void run() {
        start = System.currentTimeMillis();
        Problem.State s = problem.initialState();
        openStates.add(s);
        maxMemoryUse++;

        if(problem.goalTest(s)) {
            showResult(s);
            return;
        }

        while (!openStates.isEmpty()){
            s = getNextState();

            if(problem.goalTest(s)) {
                showResult(s);
                return;
            }
            expandedNodesNo++;
            // visitedNodesNo++;

            ArrayList<Problem.Action> actions = problem.actions(s);
            for (Problem.Action a:actions) {
                Problem.State next = problem.result(s, a);
                next.updateState(s,next,problem.stepCost(s,next,a));

                if (!isExist(next)) {
                    openStates.add(next);
                    visitedNodesNo++;
                }
            }
            maxMemoryUse = Math.max(maxMemoryUse , openStates.size());
        }
    }

    @Override
    public Problem.State getNextState() {
        int selected = -1;
        float bestFn = Integer.MAX_VALUE;
        for(int i = 0 ; i < openStates.size() ; i++){
            if(openStates.get(i).fn - openStates.get(i).gn < bestFn){
                selected = i;
                bestFn = openStates.get(i).fn - openStates.get(i).gn; //update bestFn
            }
        }
        return openStates.remove(selected);
    }

    public void showResult(Problem.State finalState){
        float[] temp = problem.bestPath(finalState);
        System.out.println("NODE NUMBER EXIST: " + temp[0]);
        System.out.println("PATH COST: " + temp[1]);
        System.out.println("MAX MEMORY USAGE: " + maxMemoryUse);
        System.out.println("EXPANDED NODES: "+ expandedNodesNo);
        System.out.println("VISITED NODES: "+ visitedNodesNo);
        end = System.currentTimeMillis();
        System.out.println("RUN TIME:" + (end - start) + " MS");
    }


    public boolean checker(Problem.State s, ArrayList<Problem.State> a) {
        for (int i = 0; i < a.size(); i++) {
            CityPathProblem.State state = (CityPathProblem.State) a.get(i);
            if (state.cityName.equals(((CityPathProblem.State) s).cityName))
                return true;
        }
        return false;
    }

    public boolean isExist(Problem.State next){
        for (int i = 0; i < openStates.size(); i++) {
            Problem.State temp = openStates.get(i);
            if (temp.equalStates(next)) {
                if(next.gn < temp.gn) {
                    openStates.remove(i);
                    return false;
                }
                else
                    return true;
            }
        }
        return false;
    }
}
