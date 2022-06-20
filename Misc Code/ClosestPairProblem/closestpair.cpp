#include "closestpair.h"
#include <algorithm>
#include <cstdlib>
#include <iostream>
#include <sys/time.h>  // used to seed srand for extra credit
using namespace std;
//Subvector code from: https://www.tutorialspoint.com/getting-a-subvector-from-a-vector-in-cplusplus
template<typename T>
vector<T> create_subVector(vector<T> const &v, int m, int n) {
   auto first = v.begin() + m;
   auto last = v.begin() + n + 1;
   vector<T> vector(first, last);
   return vector;
}


Outcome get_Best_Combined(const vector<Point>& deltaStrip, Outcome best) {
    //SCAN THROUGH DELTA STRIP
    /*Outcome bestDelta = brute(deltaStrip);
    if (bestDelta.dsq < best.dsq) {
        return bestDelta;
    } else {
        return best;
    }*/

    for (unsigned int j = 0; j < deltaStrip.size()-1; j++) {
        long long temp = distSquared(deltaStrip[j+1], deltaStrip[j]);
        if (temp <= best.dsq) {
            best = Outcome(deltaStrip[j], deltaStrip[j+1], temp);
        }
    }
    return best;
}

//More efficient implementation of getBest_Recursive() which only creates a subvector with the brute case
Outcome getBest_Recursive_Efficient(const vector<Point>& data_X, const vector<Point>& data_Y, long long delta, const int start_X, const int end_X) {
    //Get size list and check
    int difference = end_X - start_X;
    if (difference <= 3) {
        //BASE CASE
        //Brute force the subvector
        //cout << "Base case" << "\n";
        vector<Point> subVector = create_subVector(data_X, start_X, end_X);
        return brute(subVector);
    } else {
        //RECURSIVE CASE
        const int mid_X = end_X/2 + start_X/2;
        //Create the list of Y's to pass onto the recursion
        vector<Point> left_Y;
        vector<Point> right_Y;
        for (unsigned int i = 0; i < data_Y.size(); i++) {
            if (data_Y[i].x < data_X[mid_X].x) {
                left_Y.push_back(data_Y[i]);
            } else {
                right_Y.push_back(data_Y[i]);
            }
        }

        //Call the recursion using start/mid/end
        Outcome best_X;
        Outcome best_Left;
        best_Left = getBest_Recursive_Efficient(data_X, left_Y, delta, start_X, mid_X);
        Outcome best_Right;
        best_Right = getBest_Recursive_Efficient(data_X, right_Y, delta, mid_X, end_X);
        //Compare left and right outcomes to determine the smallest
        //Assumption: both best_Right and best_Left are accurate
        if (delta == -1 || (best_Right.dsq < delta || best_Left.dsq < delta)) {
            if (best_Left.dsq < best_Right.dsq) {
                best_X = best_Left;
            } else {
                best_X = best_Right;
            }
        }

        delta = best_X.dsq;

        //COMBINATION STEP
        Outcome best;
        vector<Point> deltaStrip;
        //Need a non-cheating method
        deltaStrip.push_back(data_X[mid_X]);
		//Take difference of X's, square and compare
        //Add items to Y list
        for (unsigned int k = 0; k < data_Y.size(); k++) {
            if (data_Y[k].x != data_X[mid_X].x) {
                //Pro-tip: use longs instead of ints because you know big numbers go vroom
                long long difference = data_Y[k].x - data_X[mid_X].x;
                if (difference * difference <= best_X.dsq) {
                    //cout << "Adding from Y: " << dataY[k].x << "\n";
                    deltaStrip.push_back(data_Y[k]);
                }
            }
        }

        //Using delta list, get the best of the combined list
        best = get_Best_Combined(deltaStrip, best_X);

        return best;
    }
}

//Original recursive formulation - creates subvectors with each recursive call
//For some reason this doesn't always return the correct answer? Idk the other one works better
Outcome getBest_Recursive(const vector<Point>& data, long long delta, const vector<Point>& dataY) {
    //Main Comparison
    if (data.size() <= 3) {
        //BASE CASE
        return brute(data);
    } else {
        //RECURSIVE CASE
        //Divide lists in half, recurse on each half
        int start = 0;
        int mid = data.size()/2;
        int end = data.size();
        vector<Point> left_Half = create_subVector(data, start, mid);
        vector<Point> right_Half = create_subVector(data, mid + 1, end-1);

        vector<Point> left_Y;
        vector<Point> right_Y;
        for (unsigned int i = 0; i < data.size(); i++) {
            if (dataY[i].x < data[mid].x) {
                left_Y.push_back(dataY[i]);
            } else {
                right_Y.push_back(dataY[i]);
            }
        }
        
        //Check each half to determine the best
        Outcome best_X;
        Outcome best_Left;
        best_Left = getBest_Recursive(left_Half, delta, left_Y);
        Outcome best_Right;
        best_Right = getBest_Recursive(right_Half, delta, right_Y);
        //Compare left and right outcomes to determine the smalelst
        //Assumption: both best_Right and best_Left are accurate
        if (delta == -1 || (best_Right.dsq < delta || best_Left.dsq < delta)) {
            if (best_Left.dsq < best_Right.dsq) {
                best_X = best_Left;
            } else {
                best_X = best_Right;
            }
        }
        delta = best_X.dsq;
        //COMBINATION STEP
        Outcome best;
        int median = data.size()/2;
        vector<Point> deltaStrip;
        //Need a non-cheating method
        deltaStrip.push_back(data[median]);
		//Take difference of X's, square and compare
        //Add items to Y list
        for (unsigned int k = 0; k < dataY.size(); k++) {
            if (dataY[k].x != data[median].x) {
                //Pro-tip: use longs instead of ints because you know big numbers go vroom
                long long difference = dataY[k].x - data[median].x;
                if (difference * difference <= best_X.dsq) {
                    //cout << "Adding from Y: " << dataY[k].x << "\n";
                    deltaStrip.push_back(dataY[k]);
                }
            }
        }

        
        //Cheating method
        //deltaStrip = create_subVector(data, median - (median/3), median + (median/3));
        //sort(deltaStrip.begin(), deltaStrip.end(), compareByY);
        //cout << "Size of strip: " << deltaStrip.size() << "\n";

        //Using delta list, get the best of the combined list
        best = get_Best_Combined(deltaStrip, best_X);

        return best;
    }
}

Outcome brute(const vector<Point>& data) {
    Outcome best(data[0], data[1], distSquared(data[0],data[1]));
    for (unsigned int j=1; j < data.size(); j++) {
        for (unsigned int k=0; k < j; k++) { 
            long long temp = distSquared(data[j], data[k]);
            if (temp < best.dsq) {
                best = Outcome(data[j], data[k], temp);
            }
        }
    }
    return best;
}

Outcome efficient(const vector<Point>& data) {

    //SET-UP FOR RECURSION
    //Create a list of values sorted by X
    int size = data.size();
    long long delta = -1;
    vector<Point> sorted_X(size);
    sorted_X = data;
    sort(sorted_X.begin(), sorted_X.end(), compareByX);
    vector<Point> sorted_Y(size);
    sorted_Y = data;
    sort(sorted_Y.begin(), sorted_Y.end(), compareByY);
    //Call recursive function on sorted data
    //return getBest_Recursive(sorted_X, delta, sorted_Y);
    return getBest_Recursive_Efficient(sorted_X, sorted_Y, delta, 0, size);
}

Outcome extra(const vector<Point>& data) {
    return Outcome();
}