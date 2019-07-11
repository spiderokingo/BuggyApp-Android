package scb.academy.jinglebell.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import scb.academy.jinglebell.R
import scb.academy.jinglebell.activity.SongInfoActivity
import scb.academy.jinglebell.adapter.SongAdapter
import scb.academy.jinglebell.service.ApiManager
import scb.academy.jinglebell.vo.Song
import scb.academy.jinglebell.vo.SongSearchResult

class SongListFragment : Fragment() {

    private lateinit var rvSongs: RecyclerView
    private lateinit var songAdapter: SongAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_song_list, container, false)
    }

    private val songClick: (Song) -> Unit = {
        SongInfoActivity.startActivity(context!!, it)
    }

    private val songListCallback = object : Callback<SongSearchResult> {
        override fun onFailure(call: Call<SongSearchResult>, t: Throwable) {
            Log.e("networking", "Can not call song list", t)
        }

        override fun onResponse(call: Call<SongSearchResult>, response: Response<SongSearchResult>) {
            Log.i("networking", "${response.body()}")
            songAdapter.submitList(response.body()?.results ?: listOf())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSongs = view.findViewById(R.id.rv_rooms)
        rvSongs.apply {
            adapter = SongAdapter(onClick = songClick)
                .also { songAdapter = it }
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }

        loadSongs()
    }

    private fun loadSongs()  {
        ApiManager.artistService.songs().enqueue(songListCallback)
    }
}
