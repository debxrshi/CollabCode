## Sample programs to test execution

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int n = 1000; // Find all primes less than 1000
        boolean[] isPrime = new boolean[n];
        Arrays.fill(isPrime, true);
        isPrime[0] = isPrime[1] = false;

        for (int i = 2; i * i < n; i++) {
            if (isPrime[i]) {
                for (int j = i * i; j < n; j += i) {
                    isPrime[j] = false;
                }
            }
        }

        for (int i = 2; i < n; i++) {
            if (isPrime[i]) {
                System.out.println(i + " ");
            }
        }
    }
}


```
---
```cpp
#include <iostream>
#include <vector>
#include <queue>

using namespace std;

const int INF = 1e9;

vector<vector<pair<int, int>>> adj;

void dijkstra(int start, vector<int>& dist) {
    priority_queue<pair<int, int>, vector<pair<int, int>>, greater<pair<int, int>>> pq;
    dist[start] = 0;
    pq.push({0, start});

    while (!pq.empty()) {
        int d = pq.top().first;
        int u = pq.top().second;
        pq.pop();

        if (d != dist[u])
            continue;

        for (auto edge : adj[u]) {
            int v = edge.first;
            int len = edge.second;

            if (dist[u] + len < dist[v]) {
                dist[v] = dist[u] + len;
                pq.push({dist[v], v});
            }
        }
    }
}

int main() {
    int n = 5;
    adj.resize(n);

    // Example graph
    adj[0].push_back({1, 10});
    adj[0].push_back({4, 5});
    adj[1].push_back({2, 1});
    adj[2].push_back({3, 4});
    adj[3].push_back({0, 7});
    adj[4].push_back({1, 3});
    adj[4].push_back({2, 9});
    adj[4].push_back({3, 2});

    vector<int> dist(n, INF);
    dijkstra(0, dist);

    for (int i = 0; i < n; i++) {
        cout << "Distance to node " << i << " is " << dist[i] << endl;
    }

    return 0;
}


```


---
```golang
package main

import (
    "fmt"
    "time"
)

func task(name string, ch chan string) {
    time.Sleep(time.Second * 2)
    ch <- fmt.Sprintf("Task %s completed", name)
}

func main() {
    ch := make(chan string)
    go task("A", ch)
    go task("B", ch)

    fmt.Println(<-ch) // Receive message from any goroutine
    fmt.Println(<-ch) // Receive message from any goroutine (may print in different order)
}
```
---
```python
import cmath

def fft(x):
    N = len(x)
    if N <= 1:
        return x
    even = fft(x[0::2])
    odd = fft(x[1::2])
    T = [cmath.exp(-2j * cmath.pi * k / N) * odd[k] for k in range(N // 2)]
    return [even[k] + T[k] for k in range(N // 2)] + [even[k] - T[k] for k in range(N // 2)]

# Test the FFT function
x = [cmath.exp(2j * cmath.pi * i / 8) for i in range(8)]
fft_result = fft(x)

# Print the results
for value in fft_result:
    print(value)


```
---
```c
#include <stdio.h>
#include <stdlib.h>

#define N 4

void multiply(int a[N][N], int b[N][N], int result[N][N]) {
    int i, j, k;
    for (i = 0; i < N; i++) {
        for (j = 0; j < N; j++) {
            result[i][j] = 0;
            for (k = 0; k < N; k++) {
                result[i][j] += a[i][k] * b[j][k];
            }
        }
    }
}

int main() {
    int a[N][N] = {
        {1, 2, 3, 4},
        {5, 6, 7, 8},
        {9, 10, 11, 12},
        {13, 14, 15, 16}
    };
    int b[N][N] = {
        {16, 15, 14, 13},
        {12, 11, 10, 9},
        {8, 7, 6, 5},
        {4, 3, 2, 1}
    };
    int result[N][N];

    multiply(a, b, result);

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < N; j++) {
            printf("%d ", result[i][j]);
        }
        printf("\n");
    }

    return 0;
}

```
---
```rust
fn quicksort(arr: &mut [i32]) {
    if arr.len() <= 1 {
        return;
    }

    let pivot_index = partition(arr);
    quicksort(&mut arr[0..pivot_index]);
    quicksort(&mut arr[pivot_index + 1..arr.len()]);
}

fn partition(arr: &mut [i32]) -> usize {
    let pivot_index = arr.len() / 2;
    arr.swap(pivot_index, arr.len() - 1);

    let mut store_index = 0;
    for i in 0..arr.len() - 1 {
        if arr[i] < arr[arr.len() - 1] {
            arr.swap(i, store_index);
            store_index += 1;
        }
    }
    arr.swap(store_index, arr.len() - 1);
    store_index
}

fn main() {
    let mut arr = [3, 6, 8, 10, 1, 2, 1];
    quicksort(&mut arr);
    println!("{:?}", arr);
}


```